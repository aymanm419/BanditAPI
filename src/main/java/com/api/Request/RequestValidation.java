package com.api.Request;

import java.util.Set;

public class RequestValidation {
    public RequestValidation() {

    }

    public boolean queryParameterExists(Set<String> queryParams, String param) {
        return queryParams.contains(param);
    }

    public boolean isParametersMissing(Set<String> queryParams, String... params) {
        for (String param : params) {
            if (!queryParameterExists(queryParams, param))
                return true;
        }
        return false;
    }
}
