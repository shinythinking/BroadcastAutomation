package com.solapi.sdk.message.model.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountForCharge(
    val sms: Map<String, Int>? = null,
    val lms: Map<String, Int>? = null,
    val mms: Map<String, Int>? = null,
    val ata: Map<String, Int>? = null,
    val cta: Map<String, Int>? = null,
    val cti: Map<String, Int>? = null,
    val nsa: Map<String, Int>? = null,
    @SerialName("rcs_sms")
    val rcsSms: Map<String, Int>? = null,
    @SerialName("rcs_lms")
    val rcsLms: Map<String, Int>? = null,
    @SerialName("rcs_mms")
    val rcsMms: Map<String, Int>? = null,
    @SerialName("rcs_tpl")
    val rcsTpl: Map<String, Int>? = null,
    @SerialName("rcs_itpl")
    val rcsItpl: Map<String, Int>? = null,
    @SerialName("rcs_ltpl")
    val rcsLtpl: Map<String, Int>? = null,
    val fax: Map<String, Int>? = null,
    val voice: Map<String, Int>? = null,
    @SerialName("bms_text")
    val bmsText: Map<String, Int>? = null,
    @SerialName("bms_image")
    val bmsImage: Map<String, Int>? = null,
    @SerialName("bms_wide")
    val bmsWide: Map<String, Int>? = null,
    @SerialName("bms_wide_item_list")
    val bmsWideItemList: Map<String, Int>? = null,
    @SerialName("bms_carousel_feed")
    val bmsCarouselFeed: Map<String, Int>? = null,
    @SerialName("bms_premium_video")
    val bmsPremiumVideo: Map<String, Int>? = null,
    @SerialName("bms_commerce")
    val bmsCommerce: Map<String, Int>? = null,
    @SerialName("bms_carousel_commerce")
    val bmsCarouselCommerce: Map<String, Int>? = null
)
