/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.sea;

import org.docksidestage.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author x-zeng
 */
public class SeaLandForm {

    @Required
    public CDef.PaymentMethod pay;
}
