/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.lido.sea;

import org.docksidestage.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author x-zeng
 */
public class LidoSeaBody {

    public Integer productId;
    public String productName;
    @Required
    public CDef.PaymentMethod pay;
}
