package com.example.adventuremessenger.models


class chatMessage(val id: String, val text: String, val fromId: String,val toId: String, val timestamp: Long){

    constructor(): this("","","", "", -1)

}