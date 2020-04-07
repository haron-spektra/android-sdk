package co.spektra.checkout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import co.spektra.checkout.interfaces.ApiInterface;
import co.spektra.checkout.models.AuthResponse;
import co.spektra.checkout.models.CheckoutRequest;
import co.spektra.checkout.models.CheckoutResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Spektra {
    private final static String CHECKOUT_URL = "https://checkout.spektra.co/";
    private final static String INITIATE_CHECKOUT_URL = "https://api.spektra.co/api/v1/";
    private final static String AUTH_URL = "https://api.spektra.co/";

    private static final String TAG = "SPK";

    private String publicKey;
    private String secretKey;
    private Activity activity;


    public Spektra(Activity activity, String public_key, String secret_key){
        publicKey=public_key;
        secretKey=secret_key;
        this.activity = activity;
    }

    public void checkout(String currency, BigDecimal amount, String description, String spektraAccountName, String successURL, String cancelURL) {
        CheckoutRequest checkoutRequest = new CheckoutRequest(currency, amount, description, spektraAccountName, successURL, cancelURL);
        authenticate(checkoutRequest);
    }

    private void authenticate(final CheckoutRequest request){
        String encoded = null;
        try {
            String appKeySecret = publicKey + ":" + secretKey;
            byte[] data = appKeySecret.getBytes("UTF-8");
            encoded = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (IOException e){
            Log.d("Exception", e.getLocalizedMessage());
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface mApiInterface = retrofit.create(ApiInterface.class);
        Call<AuthResponse> call = mApiInterface.auth( "Basic " + encoded);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful())  {
                    Log.d(TAG, "Auth response: " + response.body().getAccessToken());
                    sendCheckout(request, response.body().getAccessToken());
                }else{
                    Log.d(TAG, "onResponse: there is no response");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "failure: " + t.toString());
            }
        });
    }

    private void sendCheckout(CheckoutRequest request, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(INITIATE_CHECKOUT_URL)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface mApiInterface = retrofit.create(ApiInterface.class);
        Call<CheckoutResponse> call = mApiInterface.checkout(request, "bearer " + token);

        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {

                if (response.isSuccessful())  {
                    Log.d(TAG, "checkout id: " + response.body().getCheckoutID());
                    spektraCheckout(response.body().getCheckoutID());
                }else{
                    Log.d(TAG, "onResponse: there is no response");
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                Log.e(TAG, "failure: " + t.toString());
            }
        });
    }


    private void spektraCheckout(String checkoutId){
        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(CHECKOUT_URL + checkoutId));
        activity.startActivity(intent);
    }

    private static OkHttpClient okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }

}
