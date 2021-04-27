package com.ntnu.gidd.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResponse extends Response{
      private final Object data;

      public ValidationResponse(String message, Object data) {
            super(message);
            this.data = data;
      }
}
