package simonxianyu.shiroredis;

/**
 *
 * Created by simon on 16/8/11.
 *
 */
public interface BeanSerializer<ST> {

  ST serialize(Object ori);

  <T> T deserialize(ST s);
}
