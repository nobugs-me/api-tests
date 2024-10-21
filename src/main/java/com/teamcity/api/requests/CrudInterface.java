package com.teamcity.api.requests;

import com.teamcity.api.models.BaseModel;

public interface CrudInterface {

    Object create(BaseModel model);

    Object read(String id);

    Object update(String id, BaseModel model);

    Object delete(String id);

}
