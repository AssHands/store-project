//package com.ak.store.consumer.feign;
//
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import org.antlr.v4.runtime.CharStreams;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//@Component
//public class FeignExceptionHandler implements ErrorDecoder {
//
//    private final Gson gson = new Gson();
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        return new ExternalApiErrorResponse(
//                createInternalError(response, new InternalApiErrorResponse()));
//    }
//
//    private InternalApiErrorResponse createInternalError(Response response, InternalApiErrorResponse internalError) {
//        internalError.setTimestamp(System.currentTimeMillis());
//        internalError.setStatus(response.status());
//
//        String jsonString = null;
//        Reader reader = null;
//
//        try {
//            reader = response.body().asReader(Charset.defaultCharset());
//            jsonString = CharStreams.toString(reader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (reader != null)
//                    reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
//        internalError.setErrorCode(jsonObject.get("errorCode").getAsString());
//        internalError.setMessage(jsonObject.get("message").getAsString());
//
//        return internalError;
//    }
//}