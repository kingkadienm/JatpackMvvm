## proguard base file ##
-basedirectory proguard-pro

############### framework ###############
-include proguard-arouter.pro
-include proguard-normal.pro
-include proguard-okhttp3.pro


#################################### other ####################################
-keep class com.wangzs.core.component.** {*;}

#不要混淆<RxCommonParam>的所有属性与方法
-keepclasseswithmembers class com.wangzs.core.network.bean.RxCommonParam {
    <fields>;
    <methods>;
}
#不要混淆<RxCommonParam>的子类所有属性与方法
-keepclasseswithmembers class * extends com.wangzs.core.network.bean.RxCommonParam {
    <fields>;
    <methods>;
}
##不要混淆<RxBean>的所有属性与方法
-keepclasseswithmembers class com.wangzs.core.network.bean.RxBean {
    <fields>;
    <methods>;
}
-keepclasseswithmembers class * extends com.wangzs.core.network.bean.RxBean {
    <fields>;
    <methods>;
}
##不要混淆<BaseBean>所有子类的属性与方法
#-keepclasseswithmembers class * extends com.fast.framework.base.BaseBean{
#    <fields>;
#    <methods>;
#}
##不混淆<com.ff.example.data.bean>包下的类
#-keep class com.ff.example.data.bean.** {*;}
###############################################################################