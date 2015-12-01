package module

import play.api.GlobalSettings
import play.api.mvc.{RequestHeader, Result, Results}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
/**
 * Created by huodong on 15/8/3.
 */
object PlaySettings extends GlobalSettings {
  override def onHandlerNotFound(request: RequestHeader): Future[Result] = {
    Future {
      implicit val rh = request
      Results.NotFound(views.html.error404())
    }
  }
}
