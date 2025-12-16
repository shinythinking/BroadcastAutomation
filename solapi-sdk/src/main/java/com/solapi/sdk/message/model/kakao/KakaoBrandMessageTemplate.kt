package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoBrandMessageTemplate(
    // 템플릿 기본 정보
    var accountId: String? = null,
    var brandTemplateId: String? = null,
    var isDeleted: Boolean? = null,
    var name: String? = null,
    var pfId: String? = null,
    var pfGroupId: String? = null,
    var assignType: KakaoTemplateAssignType? = null,
    var adult: Boolean? = null,
    var chatBubbleType: ChatBubbleType? = null,
    var content: String? = null,
    var imageId: String? = null,
    var imageLink: String? = null,
    var header: String? = null,
    var additionalContent: String? = null,

    // 캐러셀
    var carousel: Carousel? = null,

    // 메인/서브 와이드 아이템
    var mainWideItem: WideItem? = null,
    var subWideItemList: List<WideItem>? = null,

    // 동영상
    var video: Video? = null,

    // 커머스/버튼/쿠폰
    var commerce: Commerce? = null,
    var buttons: List<Button>? = null,
    var coupon: Coupon? = null,

    // 코드/변수/복사 허용
    var codes: List<Code>? = null,
    var variables: List<Variable>? = null,
    var allowCopy: Boolean? = null,

    // 생성/수정 일시
    var dateCreated: String? = null,
    var dateUpdated: String? = null
) {

    @Serializable
    enum class ChatBubbleType {
        TEXT,
        IMAGE,
        WIDE,
        WIDE_ITEM_LIST,
        CAROUSEL_FEED,
        COMMERCE,
        CAROUSEL_COMMERCE
    }

    @Serializable
    data class Carousel(
        var head: CarouselHead? = null,
        var tail: CarouselTail? = null,
        var list: List<CarouselItem>? = null
    )

    @Serializable
    data class CarouselHead(
        var header: String? = null,
        var content: String? = null,
        var imageId: String? = null,
        var linkPc: String? = null,
        var linkMobile: String? = null,
        var linkAndroid: String? = null,
        var linkIos: String? = null
    )

    @Serializable
    data class CarouselTail(
        var linkPc: String? = null,
        var linkMobile: String? = null,
        var linkAndroid: String? = null,
        var linkIos: String? = null
    )

    @Serializable
    data class CarouselItem(
        var header: String? = null,
        var content: String? = null,
        var additionalContent: String? = null,
        var imageId: String? = null,
        var imageLink: String? = null,
        var commerce: Commerce? = null,
        var buttons: List<Button>? = null,
        var coupon: Coupon? = null
    )

    @Serializable
    data class WideItem(
        var title: String? = null,
        var imageId: String? = null,
        var linkMobile: String? = null,
        var linkPc: String? = null,
        var linkAndroid: String? = null,
        var linkIos: String? = null
    )

    @Serializable
    data class Video(
        var videoUrl: String? = null,
        var imageId: String? = null
    )

    @Serializable
    data class Commerce(
        var title: String? = null,
        // 숫자 정보를 문자열로 응답하는 스키마를 그대로 따름
        var regularPrice: String? = null,
        var discountPrice: String? = null,
        var discountRate: String? = null,
        var discountFixed: String? = null
    )

    @Serializable
    data class Button(
        var name: String? = null,
        var linkType: String? = null,
        var linkMobile: String? = null,
        var linkPc: String? = null,
        var linkAndroid: String? = null,
        var linkIos: String? = null
    )

    @Serializable
    data class Coupon(
        var title: String? = null,
        var description: String? = null,
        var linkMobile: String? = null,
        var linkPc: String? = null,
        var linkAndroid: String? = null,
        var linkIos: String? = null
    )

    @Serializable
    data class Code(
        var code: String? = null,
        var status: CodeStatus? = null,
        var service: String? = null,
        var isMain: Boolean? = null
    )

    @Serializable
    enum class CodeStatus { ACTIVE, INACTIVE, DELETED }

    @Serializable
    data class Variable(
        var name: String? = null,
    )
}