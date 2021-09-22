/*
 * Copyright (c) 2021 LiGuo <bingyang136@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.panli.pay.service.domain.enums;

public class ConstantDefine {
    public static final String ALI_CHANNEL_PREFIX = "ali";
    public static final String WX_CHANNEL_PREFIX = "wx";

    //----------------------------ali---------------------------------------

    public static final String ALI_REFUND_CHANNEL = "aliPay.Channel.Refund";
    public static final String ALI_PAY_QUERY_CHANNEL = "aliPay.Channel.Query";
    public static final String ALI_PAY_REFUND_QUERY_CHANNEL = "aliPay.Channel.Refund.Query";

    public static final String ALI_PAY_BAR_CODE_CHANNEL = "aliBarCode.Channel.Pay";
    public static final String ALI_PAY_PC_CHANNEL = "aliPc.Channel.Pay";
    public static final String ALI_PAY_APP_CHANNEL = "aliApp.Channel.Pay";
    public static final String ALI_PAY_WAP_CHANNEL = "aliWap.Channel.Pay";
    public static final String ALI_NOTIFY_CHANNEL = "aliNotify";

    //---------------------------------wx-------------------------------------------

    public static final String WX_BARCODE_PAY_QUERY_CHANNEL = "wxBarCode.Channel.Query";
    public static final String WX_BARCODE_PAYMENT_CHANNEL = "wxBarCode.Channel.Pay";
    public static final String WX_BAR_CODE_REFUND_CHANNEL = "wxBarCode.Channel.Refund";
    public static final String WX_BAR_CODE_REFUND_QUERY_CHANNEL = "wxBarCode.Channel.Refund.Query";
    public static final String WX_V2_CANCEL_PAYMENT = "wxBarCode.Channel.Cancel";

    public static final String WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH = "wxServiceMchBarCode.Channel.Pay";
    public static final String WX_BARCODE_PAYMENT_CHANNEL_SERVICE_MCH_CHANNEL = "wxServiceMchBarCode.Channel.Channel";
    public static final String WX_BARCODE_PAYMENT_QUERY_SERVICE_MCH = "wxServiceMchBarCode.Channel.Query";

    public static final String WX_FZ_REQUEST = "wxServiceMchFz.Channel.Request";
    public static final String WX_FZ_QUERY = "wxServiceMchFz.Channel.Query";
    public static final String WX_FZ_REFUND = "wxServiceMchFz.Channel.Refund";

    public static final String WX_ADVANCE_PAYMENT_CHANNEL = "wxJsapi.Channel.Pay";
    public static final String WX_ADVANCE_PAYMENT_APP_CHANNEL = "wxJsapiApp.Channel.Pay";
    public static final String WX_ADVANCE_PAYMENT_H5_CHANNEL = "wxJsapiH5.Channel.Pay";

    public static final String WX_V3_REFUND_CHANNEL = "wxJsapi.Channel.Refund";
    public static final String WX_ADVANCE_PAYMENT_QUERY_CHANNEL = "wxJsapi.Channel.Query";
    public static final String WX_V3_REFUND_QUERY_CHANNEL = "wxJsapi.Channel.Refund.Query";

    public static final String YUG_BEAN_PAYMENT_CHANNEL = "yugBean.Channel.Pay";
    public static final String YUG_BEAN_PAYMENT_REFUND_CHANNEL = "yugBean.Channel.Refund";
    public static final String YUG_BEAN_PAYMENT_QUERY_CHANNEL = "yugBean.Channel.Query";

    public static final String DEFAULT_REFUND_TEMPLATE = "DefaultRefundTemplate";
    public static final String DEFAULT_CANCEL_TEMPLATE = "DefaultCancelTemplate";
    public static final String DEFAULT_PAYMENT_TEMPLATE = "DefaultPayment";
    public static final String DEFAULT_QUERY_TEMPLATE = "DefaultPaymentQuery";

    public static final String DEFAULT_PROFIT_SHARING_REQUEST_TEMPLATE = "DefaultProfitSharingTemplate";

    /**
     * is sharing ?
     */
    public static final String SERVICE_MERCHANT_BAR_CODE_PAY_QUERY_TEMPLATE = "FLOW.wxServiceMchBarCode.Query";


    public static final String YUG_BEAN_REFUND_TEMPLATE = "YUG.yugBean.Refund";


    public static final String WX_NOTIFY_CHANNEL = "WxNotify";
    public static final String WX_V2_NOTIFY_CHANNEL = "WxV2Notify";

    private ConstantDefine() {

    }
}
