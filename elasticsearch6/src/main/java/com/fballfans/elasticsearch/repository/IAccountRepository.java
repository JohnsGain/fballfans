package com.fballfans.elasticsearch.repository;

import com.fballfans.elasticsearch.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoBox;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Repository
public interface IAccountRepository extends ElasticsearchRepository<Account, Long> {

    Page<Account> findByAddress(@Nullable String address, Pageable pageable);

    Optional<List<Account>> findTop5ByAddress(@Nullable String address);

    Optional<Page<Account>> findTop5ByState(Pageable pageable, String state);

    Page<Account> findByAddressIgnoreCase(@Nullable String address, Pageable pageable);

    Page<Account> findByAddressNot(@Nullable String address, Pageable pageable);

    Page<Account> findByAddressOrAddress(@Nullable String address, @Nullable String address2, Pageable pageable);

    Page<Account> findByAddressAndAddress(@Nullable String address, @Nullable String address2, Pageable pageable);

    Page<Account> findByBalanceBetween(Double from, @Nullable Double to, Pageable pageable);

    Page<Account> findByBalanceLessThanEqual(Double bound, Pageable pageable);

    /**
     * 右模糊匹配 address like xxx*,针对分词过后的每个term，如 tom like jane,
     * 搜索参数是tom,lik,jan之一都可以搜索出结果，但是如果参数是om,ike,ane就搜索不出来
     *
     * @param bound
     * @param pageable
     * @return
     */
    Page<Account> findByAddressLike(String bound, Pageable pageable);

    Page<Account> findByAddressStartingWith(String start, Pageable pageable);

    /**
     * 需要按分词的结果进行In查询，不是字符串匹配。
     * 例如address = "blue lane street",
     * 使用 lane能搜索出这个结果，使用lan搜索不出来，因为分词结果里面没有lan这个词。上面address
     * 分词之后的词是lane
     * @param address
     * @param pageable
     * @return
     */
    Page<Account> findByAddressIn(Collection<String> address, Pageable pageable);

    /**
     * 可以进行字符串匹配
     *  例如address = "blue lane street",参数是ane,lan,lane都可以得到结果
     * @param address
     * @param pageable
     * @return
     */
    Page<Account> findByAddressContaining(String address, Pageable pageable);

    List<Account> findByGeoPoint(GeoPoint point);

    Page<Account> findByGeoPointWithin(GeoPoint point, String distance, Pageable pageable);

    Page<Account> findByGeoPointWithin(GeoPoint point, Distance distance, Pageable pageable);

    Page<Account> findByGeoPointNear(GeoBox box, Pageable pageable);

    Page<Account> findByGeoPointNear(Box box, Pageable pageable);

    Page<Account> findByGeoPointNear(Point point, Distance distance, Pageable pageable);

    Page<Account> findByGeoPointNear(GeoPoint point, String distance, Pageable pageable);

}
