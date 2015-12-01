package module.ccp
/*
import com.cloopen.rest.sdk.CCPRestSDK
import module.Config.YunTongXun

/**
 * author: huodon@gmail.com
 * created: 15/8/6
 */
object Sample {

  lazy val api = {
    val ccp = new CCPRestSDK()
    ccp.init("sandboxapp.cloopen.com", "8883")
    ccp.setAccount(YunTongXun.accountSID, YunTongXun.authToken)
    ccp.setAppId(YunTongXun.appId)
    ccp
  }

  def sendSMS() = {
    api.sendSMS("18682695659", "test")
  }

}
*/