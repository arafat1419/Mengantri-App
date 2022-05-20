package com.arafat1419.mengantri_app.core.utils

object StatusHelper {
    const val TICKET_SUCCESS = "success"
    const val TICKET_PROGRESS = "progress"
    const val TICKET_CANCEL = "cancel"
    const val TICKET_WAITING = "waiting"

    const val SERVICE_SHOW = 1
    const val SERVICE_HIDE = 0

    // For onClick function from CompanyCustomer to HomeFragment
    const val EXTRA_FRAGMENT_STATUS = "extra_fragment_status"
    const val EXTRA_TICKET_ID = "extra_ticket_id"
}