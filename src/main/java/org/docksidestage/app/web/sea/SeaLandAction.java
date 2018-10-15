/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.sea;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.app.web.base.HarborBaseAction;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author x-zeng
 */
public class SeaLandAction extends HarborBaseAction {

    // ===================================================================================
    //                                                                            Attribute
    //                                                                            ========
    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                            Execute
    //                                                                            ========
    @Execute
    public HtmlResponse index(Integer productId, SeaLandForm form) {
        validate(form, messages -> {}, () -> {
            return asHtml(path_Sea_SeaLandHtml);
        });
        Integer userId = getUserBean().get().getUserId();
        ListResultBean<Purchase> purchaseList = selectPurchaseList(productId, form, userId);
        List<SeaLandRowBean> beans = mappingToBeans(purchaseList);
        return asHtml(path_Sea_SeaLandHtml).renderWith(data -> {
            data.register("beans", beans);
        });
    }

    // ===================================================================================
    //                                                                            Mapping
    //                                                                            ========
    private List<SeaLandRowBean> mappingToBeans(ListResultBean<Purchase> purchaseList) {
        return purchaseList.stream().map(purchase -> {
            SeaLandRowBean bean = new SeaLandRowBean();
            bean.purchaseId = purchase.getPurchaseId();
            purchase.getMember().alwaysPresent(member -> {
                bean.memberName = member.getMemberName();
            });
            purchase.getProduct().alwaysPresent(product -> {
                bean.productName = product.getProductName();
                bean.productHandleCode = product.getProductHandleCode();
            });
            bean.purchaseDate = purchase.getPurchaseDatetime().toLocalDate();
            bean.purchasePrice = purchase.getPurchasePrice();
            return bean;
        }).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                            Select
    //                                                                            ========
    private ListResultBean<Purchase> selectPurchaseList(Integer productId, SeaLandForm form, Integer userId) {
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
                paymentCB.query().setPaymentMethodCode_Equal_AsPaymentMethod(form.pay);
            });
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });
    }
}
