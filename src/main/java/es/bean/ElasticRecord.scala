package es.bean

import java.util

import scala.beans.BeanProperty

/**
 * Created by jason on 15-11-11.
 */
class ElasticRecord extends Serializable {

  @BeanProperty
  var data: util.Map[_, _] = _

}
