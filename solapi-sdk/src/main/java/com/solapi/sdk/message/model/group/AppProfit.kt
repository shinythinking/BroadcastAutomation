package com.solapi.sdk.message.model.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppProfit(
    val sms: Float? = null,
    val lms: Float? = null,
    val mms: Float? = null,
    val ata: Float? = null,
    val cta: Float? = null,
    val cti: Float? = null,
    val nsa: Float? = null,
    @SerialName("rcs_sms")
    val rcsSms: Float? = null,
    @SerialName("rcs_lms")
    val rcsLms: Float? = null,
    @SerialName("rcs_mms")
    val rcsMms: Float? = null,
    @SerialName("rcs_tpl")
    val rcsTpl: Float? = null,
    @SerialName("rcs_itpl")
    val rcsItpl: Float? = null,
    @SerialName("rcs_ltpl")
    val rcsLtpl: Float? = null,
    val fax: Float? = null,
    val voice: Float? = null,
    @SerialName("bms_text")
    val bmsText: Float? = null,
    @SerialName("bms_image")
    val bmsImage: Float? = null,
    @SerialName("bms_wide")
    val bmsWide: Float? = null,
    @SerialName("bms_wide_item_list")
    val bmsWideItemList: Float? = null,
    @SerialName("bms_carousel_feed")
    val bmsCarouselFeed: Float? = null,
    @SerialName("bms_premium_video")
    val bmsPremiumVideo: Float? = null,
    @SerialName("bms_commerce")
    val bmsCommerce: Float? = null,
    @SerialName("bms_carousel_commerce")
    val bmsCarouselCommerce: Float? = null
)
