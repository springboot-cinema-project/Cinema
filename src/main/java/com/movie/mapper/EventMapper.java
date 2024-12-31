package com.movie.mapper;

import com.movie.domain.Events;
import com.movie.domain.MemberCoupon;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventMapper {

    @Insert("INSERT INTO events (event_title, event_content, event_img, event_start_date, event_end_date, coupon_id)" +
            " VALUES (#{eventTitle}, #{eventContent}, #{eventImg}, #{eventStartDate}, #{eventEndDate}, #{couponId})")
    public long insertEvent(Events events);

    @Insert("INSERT INTO memberCoupon (event_title, event_content, event_img, event_start_date, event_end_date, coupon_id, user_id, coupon_price)" +
            " VALUES (#{eventTitle}, #{eventContent}, #{eventImg}, #{eventStartDate}, #{eventEndDate}, #{couponId}, #{userId}, #{couponPrice})")
    public long memberCoupon(MemberCoupon coupon);

    public List<Events> eventManageList();

    @Select("SELECT * FROM events WHERE id = #{id}")
    public Events eventDetail(long id);

    public long updateEvent(Events events);

    @Delete("DELETE FROM events WHERE id = #{id}")
    public long deleteEvent(long id);

    public List<Events> eventStart();

    public List<Events> eventEnd();



}
