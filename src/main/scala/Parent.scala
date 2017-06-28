

class Parent extends DeepCloneable[Parent] {
  var l = List("111","2222","33333")
  var s = "abc"

  def deepClone(): Parent = {
    val p = new Parent()
    p.l = this.l
    p.s = this.s
    p
  }
}