package models.update;

import java.util.List;

public record FailureUpdateResponseBodyModel(
        String detail,
        String code,
        List<Object> messages
) {}
