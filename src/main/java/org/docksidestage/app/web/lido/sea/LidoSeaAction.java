/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.lido.sea;

import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.app.web.base.HarborBaseAction;
import org.docksidestage.app.web.lido.sea.LidoSeaResult.LidoSeaProductPart;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author x-zeng
 */
public class LidoSeaAction extends HarborBaseAction {

    // ===================================================================================
    //                                                                            Attribute
    //                                                                            ========
    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                            Execute
    //                                                                            ========
    @Execute
    public JsonResponse<LidoSeaResult> index(Integer productId, LidoSeaBody body) {
        validateApi(body, messages -> {});
        Integer userId = getUserBean().get().getUserId();
        ListResultBean<Purchase> purchaseList = selectPurchaseList(productId, body, userId);
        LidoSeaResult result = mappingToResult(purchaseList);
        return asJson(result);
    }

    // ===================================================================================
    //                                                                            Mapping To Result
    //                                                                            ========
    private LidoSeaResult mappingToResult(ListResultBean<Purchase> purchaseList) {
        LidoSeaResult result = new LidoSeaResult();
        result.products = purchaseList.stream().map(purchase -> {
            LidoSeaProductPart part = new LidoSeaProductPart();
            part.purchaseId = purchase.getPurchaseId();
            purchase.getMember().alwaysPresent(member -> {
                part.memberName = member.getMemberName();
            });
            purchase.getProduct().alwaysPresent(product -> {
                part.productName = product.getProductName();
                part.productHandleCode = product.getProductHandleCode();
            });
            part.purchaseDate = purchase.getPurchaseDatetime().toLocalDate();
            part.purchasePrice = purchase.getPurchasePrice();
            return part;
        }).collect(Collectors.toList());
        result.yourMood = "オープンリーチ一発ツモされて満貫の親っかぶりした気分";
        return result;
    }

    // ===================================================================================
    //                                                                            Select
    //                                                                            ========
    private ListResultBean<Purchase> selectPurchaseList(Integer productId, LidoSeaBody body, Integer userId) {
        return purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            cb.query().setProductId_Equal(productId);
            cb.orScopeQuery(orCB -> {
                orCB.query().setMemberId_Equal(userId);
                orCB.query().queryMember().existsMemberFollowingByYourMemberId(followingCB -> {
                    followingCB.query().setMyMemberId_Equal(userId);
                });
            });
            cb.query().existsPurchasePayment(paymentCB -> {
                paymentCB.query().setPaymentMethodCode_Equal_AsPaymentMethod(body.pay);
            });
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });
    }
}
