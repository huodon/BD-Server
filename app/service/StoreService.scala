package service

/**
 * Created by huodong on 15/7/28.
 */

import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject._

import model.slick.Tables
import module.slick.SlickConfig
import play.api.Logger.{logger => LOG}
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
@Singleton
class StoreService @Inject()(val reactiveMongoApi: ReactiveMongoApi) {


  import slick.driver.PostgresDriver.api._
  val db = SlickConfig.dbConfig.db
  val fs = reactiveMongoApi.gridFS

  fs.ensureIndex().onComplete {
    case Success(index) =>
      LOG.debug(s"Checked index, result is $index")
    case Failure(t) => LOG.error(t.getMessage)

  }
  // TODO https://github.com/sgodbillon/reactivemongo-demo-app/blob/master/app%2Fcontrollers%2FArticles.scala
  def save() = {
//    val e: Enumerator[Array[Byte]] = Enumerator.fromFile(new java.io.File(""))
  }




  /**
   * 文件上传函数
   * 返回文件相对路径或error
   */
  def uploadDef(file: MultipartFormData.FilePart[Files.TemporaryFile]):String={
    val sep = System.getProperty("file.separator")
    val imgs = List("bmp","gif","jpe","jpeg","jpg","png","tif","tiff","ico")
    val filename = file.filename
    var ext = filename.split("\\.")(1)
    if(imgs.contains(ext)){
      val path = createDirByDate() + sep + System.currentTimeMillis() + "." + ext
      file.ref.moveTo(new File(path))
      sep + "assets"+path.split("public")(1)
    }else{
      sep + "assets" + sep + "images" + sep + "event" + sep + "error.jpg"
    }
  }
  /**
   * 根据日期创建目录
   */
  def createDirByDate():String={
    val sep = System.getProperty("file.separator")
    val bpath = System.getProperty("user.dir")
    val path = bpath + sep + "public" + sep + "upload" + sep + new SimpleDateFormat("yyyy" + sep + "MM" + sep + "dd").format(Calendar.getInstance.getTime);
    val f = new File(path)
    if(!f.exists()){
      f.mkdirs()
    }
    path
  }


  def saveImage(name: String, meta: Option[String]) = {

    val a = Tables.ImagesStore.map( img => (img.name, img.meta)) += (name, meta)
    db.run(a)
  }

  def findImageByName(name: String) = {
    val a = Tables.ImagesStore.filter(_.name === name).result.head
    db.run(a)
  }

}
