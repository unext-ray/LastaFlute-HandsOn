/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.sea;

import java.time.LocalDate;

import org.lastaflute.web.validation.Required;

/**
 * @author x-zeng
 */
public class SeaLandRowBean {

    @Required
    public Long purchaseId;
    @Required
    public String memberName;
    @Required
    public String productName;
    @Required
    public String productHandleCode;
    @Required
    public LocalDate purchaseDate;
    @Required
    public Integer purchasePrice;
}
