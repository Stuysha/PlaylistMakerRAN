package com.example.sprint8.data.dto

open  class  Response <T> ()  {
    var resultCode = 0
    var data : T? = null
}