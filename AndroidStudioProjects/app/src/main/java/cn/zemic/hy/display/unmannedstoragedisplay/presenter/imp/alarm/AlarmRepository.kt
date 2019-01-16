package cn.zemic.hy.display.unmannedstoragedisplay.presenter.imp.alarm

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.zemic.hy.display.unmannedstoragedisplay.model.bean.FilterLedForMobileUiCommand
import cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel.*
import cn.zemic.hy.display.unmannedstoragedisplay.network.URLFactory
import cn.zemic.hy.display.unmannedstoragedisplay.network.service.FetchDataService
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IAlarmRepository
import cn.zemic.hy.display.unmannedstoragedisplay.presenter.interfaces.IBaseRepository
import microsoft.aspnet.signalr.client.Platform
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent
import microsoft.aspnet.signalr.client.hubs.HubConnection
import microsoft.aspnet.signalr.client.hubs.HubProxy
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * @author fxs
 */
class AlarmRepository : IAlarmRepository {
    private var mHandler: MyMessagesHandler? = null
    private var mHubProxy: HubProxy? = null

    override fun getWareHouseNo(deviceId: String, onGetWareHouseNoFinish: IBaseRepository.OnGetWareHouseNoFinish) {
        val filter = FilterLedForMobileUiCommand(deviceId)
        FetchDataService.getWareHouseNoService(filter).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (!response.isSuccessful) {
                    onGetWareHouseNoFinish.onFail(String.format(Locale.CHINA, "获取仓库号响应异常，错误码：%d", response.code()))
                    return
                }
                val wareHouseNo = response.body()
                onGetWareHouseNoFinish.onSuccess(wareHouseNo)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onGetWareHouseNoFinish.onFail(if (t.message == null || "" == t.message) "获取仓库号失败" else t.message)
            }
        })
    }

    override fun connectServiceForGetWarningAndUserInfo(deviceId: String, wareHouseNo: String, onGetWarningDataFinish: IBaseRepository.OnGetWarningDataFinish) {
        mOnGetWarningDataFinish = onGetWarningDataFinish
        mHandler = MyMessagesHandler()
        Platform.loadPlatformComponent(AndroidPlatformComponent())
        val serverURI = String.format(Locale.CHINA, "%ssignalr", URLFactory.serverURI)
        //        String serverURI = "http://192.168.0.2:25535/signalr";
        val connection = object : HubConnection(serverURI) {
            override fun onConnected() {
                super.onConnected()
                Log.i("hubOnConnected", "hubOnConnected")
            }

            override fun onError(error: Throwable, mustCleanCurrentConnection: Boolean) {
                super.onError(error, mustCleanCurrentConnection)
                val message = Message.obtain()
                message.what = DATA_ERROR
                message.obj = if ("" == error.message) "连接推送服务器出现异常，请联系管理员" else error.message
                mHandler!!.sendMessage(message)
            }

            override fun onClosed() {
                super.onClosed()
            }
        }
        //hub agency
        mHubProxy = connection.createHubProxy("electronicSignsHub")
        //hubProxy.on可以理解为讯息or事件监听器
        mHubProxy!!.run {
            on("sendUserIn", this@AlarmRepository::runWithUserInfo, String::class.java, Boolean::class.java, UserInViewModel::class.java, MachineBindDoorControlViewModel::class.java)
            on("sendCheck", this@AlarmRepository::runWithIsCheck, String::class.java, Boolean::class.java)
            on("sendMaintain", this@AlarmRepository::runWithMaintain, String::class.java, Boolean::class.java)
            on("sendTemAndHum", this@AlarmRepository::runWithTemperature, String::class.java, Float::class.java, Float::class.java)
            on("sendUserLeave", this@AlarmRepository::runWithUserOut, String::class.java, UserOutVM::class.java)
            //接收告警信息
            on("sendWarnInform", this@AlarmRepository::runWithWarnInfo, String::class.java, OperateWarningInformViewModel::class.java)
            //接受日志信息
            on("sendConnectMessage", this@AlarmRepository::runWithConnectMsg, String::class.java, String::class.java, Boolean::class.java)
            on("sendAttendanceMachine", this@AlarmRepository::entranceGuardState, String::class.java, EntranceGuardState::class.java)
            on("sendLedShelfAndunit", this@AlarmRepository::shelfState, String::class.java, ShelfState::class.java)
        }
        val con = connection.start(ServerSentEventsTransport(connection.logger))
        try {
            con.get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        con.onError { error -> onError(error) }
        if (con.isDone) {
            //调用服务器连接方法
            mHubProxy!!.invoke("Connect", wareHouseNo, deviceId)
        }
    }

    override fun getHubProxy(): HubProxy {
        if (null == mHubProxy) {
            throw NullPointerException("mHubProxy is null")
        }
        return mHubProxy!!
    }

    private fun runWithUserInfo(warehouseNo: String, isOpenDoor: Boolean?, userIn: UserInViewModel, doorControl: MachineBindDoorControlViewModel) {
        val message = Message.obtain()
        message.what = USER_CHANGED
        val bundle = Bundle()
        bundle.putBoolean("isOpenDoor", isOpenDoor!!)
        bundle.putString("warehouseNo", warehouseNo)
        bundle.putParcelable("userIn", userIn)
        bundle.putParcelable("doorControl", doorControl)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }


    private fun runWithUserOut(warehouseNo: String, user: UserOutVM) {
        val message = Message.obtain()
        message.what = USER_OUT
        val bundle = Bundle()
        bundle.putString("warehouseNo", warehouseNo)
        bundle.putParcelable("userOut", user)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun runWithIsCheck(warehouseNo: String, isCheck: Boolean?) {
        val message = Message.obtain()
        message.what = CHECK
        val bundle = Bundle()
        bundle.putBoolean("isCheck", isCheck!!)
        bundle.putString("warehouseNo", warehouseNo)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun runWithMaintain(warehouseNo: String, isMaintain: Boolean?) {
        val message = Message.obtain()
        message.what = MAINTAIN
        val bundle = Bundle()
        bundle.putBoolean("isMaintain", isMaintain!!)
        bundle.putString("warehouseNo", warehouseNo)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun runWithTemperature(wareHouseNo: String, temperature: Float, humidity: Float) {
        val message = Message.obtain()
        message.what = TEMPERATURE_HUMIDITY
        val bundle = Bundle()
        bundle.putString("wareHouseNo", wareHouseNo)
        bundle.putFloat("temperature", temperature)
        bundle.putFloat("humidity", humidity)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun runWithWarnInfo(warehouseNo: String, warningInform: OperateWarningInformViewModel) {
        val message = Message.obtain()
        message.what = DATA_CHANGED
        val bundle = Bundle()
        bundle.putString("warehouseNo", warehouseNo)
        bundle.putParcelable("warningInform", warningInform)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun runWithConnectMsg(connectionMsg: String, connectionId: String, isConnect: Boolean) {
        val message = Message.obtain()
        message.what = DATA_SEND_MSG
        val bundle = Bundle()
        if (isConnect) {
            mConnectionId = connectionId
        }
        bundle.putString("connectionId", connectionId)
        bundle.putBoolean("isConnect", isConnect)
        message.data = bundle
        message.obj = connectionMsg
        mHandler!!.sendMessage(message)
    }

    private fun entranceGuardState(warehouseNo: String, entranceGuardState: EntranceGuardState) {
        val message = Message.obtain()
        message.what = ENTRANCE_GUARD_STATE
        val bundle = Bundle()
        bundle.putString("warehouseNo", warehouseNo)
        bundle.putParcelable("entranceGuardState", entranceGuardState)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun shelfState(warehouseNo: String, shelfState: ShelfState) {
        val message = Message.obtain()
        message.what = SHELF_STATE
        val bundle = Bundle()
        bundle.putString("warehouseNo", warehouseNo)
        bundle.putParcelable("shelfState", shelfState)
        message.data = bundle
        mHandler!!.sendMessage(message)
    }

    private fun onError(error: Throwable) {
        val message = Message.obtain()
        message.what = DATA_ERROR
        message.obj = if ("" == error.message) error.message else "连接中断，请联系管理员"
        mHandler!!.sendMessage(message)
    }

    private class MyMessagesHandler : Handler() {
        private val LIST_MAX_SIZE = 10
        private val mOperateWarningInformViewModels = ArrayList<OperateWarningInformViewModel>()

        override fun handleMessage(msg: Message) {
            if (mOnGetWarningDataFinish == null) {
                return
            }
            super.handleMessage(msg)
            when (msg.what) {
                TEMPERATURE_HUMIDITY -> {
                    val bundletempAndHum = msg.data
                    val temperature = bundletempAndHum.getFloat("temperature")
                    val humidity = bundletempAndHum.getFloat("humidity")
                    val wareHouseForTemp = bundletempAndHum.getString("wareHouseNo")
                    mOnGetWarningDataFinish!!.onFetchTemperatureAndHumidityFinish(wareHouseForTemp, temperature, humidity)
                }
                MAINTAIN -> {
                    val bundleMaintain = msg.data
                    val warehouseNoForMaintain = bundleMaintain.getString("warehouseNo")
                    val isMaintain = bundleMaintain.getBoolean("isMaintain")
                    mOnGetWarningDataFinish!!.onRunningMaintain(warehouseNoForMaintain, isMaintain)
                }
                CHECK -> {
                    val bundleCheck = msg.data
                    val warehouseNo = bundleCheck.getString("warehouseNo")
                    val isCheck = bundleCheck.getBoolean("isCheck")
                    mOnGetWarningDataFinish!!.onRunningCheck(warehouseNo, isCheck)
                }

                DATA_SEND_MSG -> {
                    val bundleMsg = msg.data
                    val connectionId = bundleMsg.getString("connectionId")
                    val isConnect = bundleMsg.getBoolean("isConnect")
                    Log.e("DATA_SEND_MSG", msg.obj.toString() + connectionId!!)
                    if (isConnect) {
                        mOnGetWarningDataFinish!!.onShow(msg.obj.toString())
                    } else {
                        if (mConnectionId == connectionId) {
                            mOnGetWarningDataFinish!!.onShow(msg.obj.toString())
                        }
                    }
                }
                DATA_ERROR -> {
                    Log.e("DATA_ERROR", msg.obj.toString())
                    mOnGetWarningDataFinish!!.onShow(msg.obj.toString())
                }
                DATA_CHANGED -> {
                    val bundle = msg.data
                    val wainingInfo = bundle.getParcelable<OperateWarningInformViewModel>("warningInform")
                    if (wainingInfo != null) {
                        if (mOperateWarningInformViewModels.size < LIST_MAX_SIZE) {
                            mOperateWarningInformViewModels.add(0, wainingInfo)
                        } else {
                            mOperateWarningInformViewModels.removeAt(mOperateWarningInformViewModels.size - 1)
                            mOperateWarningInformViewModels.add(0, wainingInfo)
                        }
                    }
                    //update view
                    if (mOperateWarningInformViewModels.size <= 1) {
                        mOnGetWarningDataFinish!!.onSuccess(mOperateWarningInformViewModels, true)
                    } else {
                        mOnGetWarningDataFinish!!.onSuccess(mOperateWarningInformViewModels, false)
                    }
                }
                USER_CHANGED -> {
                    val userBundle = msg.data
                    val isOpenDoor = userBundle.getBoolean("isOpenDoor")
                    val userIn = userBundle.getParcelable<UserInViewModel>("userIn")
                    val userWarehouseNo = userBundle.getString("warehouseNo")
                    mOnGetWarningDataFinish!!.onUserChange(userIn, userWarehouseNo, isOpenDoor)
                }
                USER_OUT -> {
                    val userOutBundle = msg.data
                    val userOut = userOutBundle.getParcelable<UserOutVM>("userOut")
                    val userOutWarehouseNo = userOutBundle.getString("warehouseNo")
                    if (null == userOut) {
                    }
                    mOnGetWarningDataFinish!!.onUserOut(userOutWarehouseNo, userOut)
                }
                ENTRANCE_GUARD_STATE -> {
                    val doorState = msg.data
                    val doorWarehouseNo = doorState.getString("warehouseNo")
                    val entranceGuardState = doorState.getParcelable<EntranceGuardState>("entranceGuardState")
                    mOnGetWarningDataFinish!!.onReceiveDoorState(doorWarehouseNo, entranceGuardState)
                }
                SHELF_STATE -> {
                    val shelfStateBundle = msg.data
                    var shelfWarehouseNo = shelfStateBundle.getString("warehouseNo")
                    shelfWarehouseNo = shelfWarehouseNo ?: ""
                    val shelfState = shelfStateBundle.getParcelable<ShelfState>("shelfState")
                    shelfState?.Shelfs?.sortBy {
                        it.shelfNo
                    }
                    mOnGetWarningDataFinish!!.onReceiveShelfState(shelfWarehouseNo, shelfState)
                }
                else -> {
                }
            }
        }
    }

    companion object {
        private const val DATA_ERROR = 0
        private const val DATA_CHANGED = 1
        private const val USER_CHANGED = 2
        private const val DATA_SEND_MSG = 3
        private const val USER_OUT = 4
        private const val CHECK = 5
        private const val MAINTAIN = 6
        private const val TEMPERATURE_HUMIDITY = 7
        private const val ENTRANCE_GUARD_STATE = 8
        private const val SHELF_STATE = 9

        private var mOnGetWarningDataFinish: IBaseRepository.OnGetWarningDataFinish? = null
        private var mConnectionId: String? = null
    }
}

