package models.domain;

import java.io.Serializable;
import java.util.Map;

public interface DataPersistenza<T> extends Serializable {

    Map<String, Object> toMap();

    T fromMap(Map<String, Object> fromDatabaseMap);

}
