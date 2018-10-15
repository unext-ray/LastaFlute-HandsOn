/**
 * Copyright(c) u-next.
 */
package org.docksidestage.app.web.lido.sea;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.Required;

/**
 * @author x-zeng
 */
public class LidoSeaResult {

    @NotNull
    @Valid
    public List<LidoSeaProductPart> products;

    public static class LidoSeaProductPart {

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

    @Required
    public String yourMood;
}
