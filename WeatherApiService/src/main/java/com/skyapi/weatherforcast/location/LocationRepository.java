package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location,String> {
}
