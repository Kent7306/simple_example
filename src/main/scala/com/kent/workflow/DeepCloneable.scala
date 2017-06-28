package com.kent.workflow

trait DeepCloneable[A] {
    def deepClone():A
}