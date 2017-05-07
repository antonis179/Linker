package org.amoustakos.linker.ddos;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import org.amoustakos.linker.io.base.BaseRequest;

public class BlockingClient {
    private static final int NUMBER_OF_REQUESTS = 100;
    private static final String url = "http://127.0.0.1:8090/Link";

    private static final Gson gson = new Gson();
    static final String payload = gson.toJson(new BaseRequest());

    public static void main(String[] args){
        Long end = doOperation(NUMBER_OF_REQUESTS);
        System.out.println("Completed " + NUMBER_OF_REQUESTS + " requests in " + end + "ms.");
    }

    public static Long doOperation(int requests){
        Long nanos = 0L;
        for(int i=0; i<requests; i++)
            nanos += sendRequest(i);

        return (nanos)/1000000;
    }

    public static Long sendRequest(int iteration){
        try {
            long start = System.nanoTime();
//            HttpResponse<JsonNode> resp =
                    Unirest.post(url).body(payload).asJson();
//            BaseResponse response = gson.fromJson(resp.getBody().getObject().toString(), BaseResponse.class);
            Long nanos = System.nanoTime() - start;
//            Long end = (nanos)/1000000;
//            System.out.println(
//                    "Completed #" + iteration + " in " + end.intValue() + "ms" + "\n" +
//                    "Response code was: " + response.getStatusCode()
//            );
            return nanos;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Iteration #" + iteration + " failed to execute");
        }
        return 0L;
    }



}
