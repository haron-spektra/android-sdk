package co.spektra.checkout.interfaces;

import co.spektra.checkout.models.AuthResponse;
import co.spektra.checkout.models.CheckoutRequest;
import co.spektra.checkout.models.CheckoutResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("checkout/initiate")
    Call<CheckoutResponse> checkout(@Body CheckoutRequest request, @Header("Authorization") String token);

    @POST("oauth/token?grant_type=client_credentials")
    Call<AuthResponse> auth(@Header("Authorization") String token);
}
