package com.fjx.prize.commons.db.mapper;

import com.fjx.prize.commons.db.entity.CardProductDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GameLoadMapper {
   /**
    * @Description 根据活动id 查询奖品信息（奖品，奖品个数，）
    * @Param [gameId]
    * @return java.util.List<com.fjx.prize.commons.db.entity.CardProductDto>
    * @Author fjx
    * @Date 2020-08-18 20:06
    **/
    @Select("select p.*,cgp.amount from card_product p " +
            "join card_game_product cgp on p.id=cgp.productid " +
            "where cgp.gameid=#{gameId,jdbcType=INTEGER}")
    public List<CardProductDto> getByGameId(int gameId);
}
