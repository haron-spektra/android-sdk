package co.spektra.checkout.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CheckoutRequest {
    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("spektraAccountName")
    @Expose
    private String spektraAccountName;

    @SerializedName("successURL")
    @Expose
    private String successURL;

    @SerializedName("cancelURL")
    @Expose
    private String cancelURL;

    @SerializedName("amount")
    @Expose
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCancelURL() {
        return cancelURL;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getSpektraAccountName() {
        return spektraAccountName;
    }

    public String getSuccessURL() {
        return successURL;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCancelURL(String cancelURL) {
        this.cancelURL = cancelURL;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpektraAccountName(String spektraAccountName) {
        this.spektraAccountName = spektraAccountName;
    }

    public void setSuccessURL(String successURL) {
        this.successURL = successURL;
    }

    public CheckoutRequest(String currency, BigDecimal amount, String description, String spektraAccountName, String successURL, String cancelURL){
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.spektraAccountName = spektraAccountName;
        this.successURL = successURL;
        this.cancelURL = cancelURL;
    }
}
