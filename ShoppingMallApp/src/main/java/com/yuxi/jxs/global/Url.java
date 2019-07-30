package com.yuxi.jxs.global;

public class Url {
//        public static final String HOST_URL = "http://192.168.3.214:8098";
//    public static final String HOST_URL = "http://192.168.3.200:83";
//    public static final String HOST_URL = "http://develop.0x0100.com:83";
    public static final String HOST_URL = "https://vip.yuximi.com";
    //登录
    public static final String LOGIN_URL = HOST_URL + "/index/user.login/login_in";

    //退出账号
    public static final String EXIT_LOGIN_URL = HOST_URL + "/index/user.login/login_out";

    //品类页面
    public static final String PRODUCT_HOME = HOST_URL + "/index/homepage/api_index";

    //顶级分类下的产品列表+搜索
//    public static final String TOP_CLASS_PRODUCT = HOST_URL + "/index/shop.product/top_class_product";
    //pc商品列表页
    public static final String TOP_CLASS_PRODUCT = HOST_URL + "/index/shop.product/product_list_by_class";

    //pc首页产品搜索
    public static final String PRODUCT_SEARCH = HOST_URL + "/index/shop.product/product_search";

    //商品详情
    public static final String PRODUCT_DETAIL = HOST_URL + "/index/shop.product/product_detail";

    //地址列表
    public static final String ADDRESS_LIST = HOST_URL + "/index/user.center/address_list";

    //新增地址
    public static final String ADD_ADDRESS = HOST_URL + "/index/user.center/address_add";

    //编辑地址
    public static final String EDIT_ADDRESS = HOST_URL + "/index/user.center/address_edit";

    //删除地址
    public static final String DELETE_ADDRESS = HOST_URL + "/index/user.center/address_del";

    //设置默认
    public static final String SET_DAFAULT = HOST_URL + "/index/user.center/set_default";

    //我的购物车商品列表
    public static final String MY_CART = HOST_URL + "/index/shop.cart/my_cart";

    //购物车商品数量加减
    public static final String CAR_PRODUCT_CHANGE = HOST_URL + "/index/shop.cart/add_cart";

    //获取我的购物车商品种类数及总价
    public static final String SHOPPING_CAR_COUNT = HOST_URL + "/index/shop.cart/cart_count";

    //删除购物车商品
    public static final String DELETE_CAR_PRODUCT = HOST_URL + "/index/shop.cart/cart_product_remove";

    //订单确认（待结算）
    public static final String CONFIRM_ORDER = HOST_URL + "/index/shop.order/wait_pay";

    //订单确认提交
    public static final String ORDER_SUBMIT = HOST_URL + "/index/shop.order/order_submit";

    //我的账户-资金
    public static final String MY_ACCOUNT = HOST_URL + "/index/user.center/my_wallet";

    //订单支付页面
    public static final String PAY_TYPE = HOST_URL + "/index/shop.order/order_pay";

    //订单支付操作
    public static final String ORDER_PAY = HOST_URL + "/index/shop.order/order_pay_submit";

    //我的订单
    public static final String MY_ORDER = HOST_URL + "/index/shop.order/my_order";

    //"我的"-主页面
    public static final String MY_PERSONAL = HOST_URL + "/index/user.center/my_index";

    //订单详情
    public static final String ORDER_DETAIL = HOST_URL + "/index/shop.order/order_detail";

    //我的账户-信用额度（仅手机端）
    public static final String MY_CREDIT = HOST_URL + "/index/user.center/my_credit";

    //获取上传图片的用户名，密码，空间名
    public static final String UPLOAD_SERVER = HOST_URL + "/index/user.center/admin_info";

    //取消订单
    public static final String CANCEL_ORDER = HOST_URL + "/index/shop.order/order_cancel";

    //修改密码发送短信
    public static final String SEND_MESSAGE = HOST_URL + "/index/user.center/send_sms";

    //修改密码操作
    public static final String CHANGE_PASSWORD = HOST_URL + "/index/user.center/change_password";
    //修改头像
    public static final String UPDATA_AVATAR = HOST_URL + "/index/user.center/change_avatar";

    //app更新接口
    public static final String APP_UPDATE = "http://imi.yuximi.com/backstage/appversion/get_appversion_data";

    //售后服务商列表
    public static final String CDEMAND_LIST = "http://imi.yuximi.com/index/cs.api/demand_list";

    //需求商下单页面
    public static final String CREATE_ORDER_PAGE = "http://imi.yuximi.com/index/cs.api/demand_order_page";

    //需求商下单提交数据
    public static final String CREATE_ORDER_SUBMIT = "http://imi.yuximi.com/index/cs.api/demand_order_create";

    //需求商总工单记录
    public static final String ORDER_RECORD_LIST = "http://imi.yuximi.com/index/cs.api/demand_order_count_list";

    //商品列表
    public static final String CRM_ORDER_PRODUCT_LIST = "http://imi.yuximi.com/index/cs.api/jxs_product";

    //需求商在某流程下的工单记录
    public static final String DEMAND_ODERD_LIST = "http://imi.yuximi.com/index/cs.api/demand_order_list";

    //工单详情页
    public static final String ORDER_SERVICE_DETAIL = "http://imi.yuximi.com/index/cs.api/service_order_view";

    //阶段页面
    public static final String SERVICE_ORDER_STEP = "http://imi.yuximi.com/index/cs.api/service_order_step";
}
