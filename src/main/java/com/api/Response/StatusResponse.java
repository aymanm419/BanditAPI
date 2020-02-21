package com.api.Response;

import lombok.Getter;
import lombok.Setter;

public enum StatusResponse {
    SUCCESS ("Success"),
    ERROR ("Error");
    @Getter
    @Setter
    private String status;

    StatusResponse(String success) {

    }

}
