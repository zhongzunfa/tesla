package io.github.tesla.authz.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.authz.domain.OauthCode;


@Repository
public class Oauth2Dao extends AuthzRowMapper {

  private static final ClientDetailsRowMapper clientDetailsRowMapper = new ClientDetailsRowMapper();
  private static final OauthCodeRowMapper oauthCodeRowMapper = new OauthCodeRowMapper();
  private static final AccessTokenRowMapper accessTokenRowMapper = new AccessTokenRowMapper();

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public ClientDetails findClientDetails(String clientId) {
    final String sql = " select * from oauth_client_details where client_id = ? ";
    final List<ClientDetails> list = jdbcTemplate.query(sql, clientDetailsRowMapper, clientId);
    return list.isEmpty() ? null : list.get(0);
  }

  public int saveClientDetails(final ClientDetails clientDetails) {
    final String sql =
        " insert into oauth_client_details(client_id,client_secret,client_name, scope,grant_types,"
            + "redirect_uri,access_token_validity,refresh_token_validity,trusted) values (?,?,?,?,?,?,?,?,?)";

    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, clientDetails.getClientId());
        ps.setString(2, clientDetails.getClientSecret());
        ps.setString(3, clientDetails.getName());
        ps.setString(4, clientDetails.scope());
        ps.setString(5, clientDetails.grantTypes());
        ps.setString(6, clientDetails.getRedirectUri());
        ps.setInt(7,
            clientDetails.accessTokenValidity() == null ? -1 : clientDetails.accessTokenValidity());
        ps.setInt(8, clientDetails.refreshTokenValidity() == null ? -1
            : clientDetails.refreshTokenValidity());
        ps.setBoolean(9, clientDetails.trusted());
      }
    });
  }


  public int saveOauthCode(final OauthCode oauthCode) {
    final String sql = " insert into oauth_code(code,username,client_id) values (?,?,?)";
    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, oauthCode.code());
        ps.setString(2, oauthCode.username());
        ps.setString(3, oauthCode.clientId());
      }
    });
  }


  public OauthCode findOauthCode(String code, String clientId) {
    final String sql = " select * from oauth_code where code = ? and client_id = ? ";
    final List<OauthCode> list = jdbcTemplate.query(sql, oauthCodeRowMapper, code, clientId);
    return list.isEmpty() ? null : list.get(0);
  }


  public OauthCode findOauthCodeByUsernameClientId(String username, String clientId) {
    final String sql = " select * from oauth_code where username = ? and client_id = ? ";
    final List<OauthCode> list = jdbcTemplate.query(sql, oauthCodeRowMapper, username, clientId);
    return list.isEmpty() ? null : list.get(0);
  }


  public int deleteOauthCode(final OauthCode oauthCode) {
    final String sql = " delete from oauth_code where code = ? and client_id = ? and username = ?";
    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, oauthCode.code());
        ps.setString(2, oauthCode.clientId());
        ps.setString(3, oauthCode.username());
      }
    });
  }


  public AccessToken findAccessToken(String clientId, String username, String authenticationId) {
    final String sql =
        " select * from oauth_access_token where client_id = ? and username = ? and authentication_id = ?";
    final List<AccessToken> list =
        jdbcTemplate.query(sql, accessTokenRowMapper, clientId, username, authenticationId);
    return list.isEmpty() ? null : list.get(0);
  }


  public int deleteAccessToken(final AccessToken accessToken) {
    final String sql =
        " delete from oauth_access_token where client_id = ? and username = ? and authentication_id = ?";
    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, accessToken.clientId());
        ps.setString(2, accessToken.username());
        ps.setString(3, accessToken.authenticationId());
      }
    });
  }


  public int saveAccessToken(final AccessToken accessToken) {
    final String sql =
        "insert into oauth_access_token(token_id,token_expired_seconds,authentication_id,"
            + "username,client_id,token_type,refresh_token_expired_seconds,refresh_token) values (?,?,?,?,?,?,?,?) ";

    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, accessToken.tokenId());
        ps.setInt(2, accessToken.tokenExpiredSeconds());
        ps.setString(3, accessToken.authenticationId());

        ps.setString(4, accessToken.username());
        ps.setString(5, accessToken.clientId());
        ps.setString(6, accessToken.tokenType());

        ps.setInt(7, accessToken.refreshTokenExpiredSeconds());
        ps.setString(8, accessToken.refreshToken());
      }
    });
  }


  public AccessToken findAccessTokenByRefreshToken(String refreshToken, String clientId) {
    final String sql =
        " select * from oauth_access_token where refresh_token = ? and client_id = ? ";
    final List<AccessToken> list =
        jdbcTemplate.query(sql, accessTokenRowMapper, refreshToken, clientId);
    return list.isEmpty() ? null : list.get(0);
  }


  public List<ClientDetails> findClientDetailsListByClientId(String clientId) {
    String sql = " select * from oauth_client_details where 1=1 ";
    if (StringUtils.isNotEmpty(clientId)) {
      sql += " and client_id = ? order by create_time desc ";
      return jdbcTemplate.query(sql, clientDetailsRowMapper, clientId);
    }
    sql += " order by create_time desc  ";
    return jdbcTemplate.query(sql, clientDetailsRowMapper);
  }

  /****** admin for ops *****/


  public Integer countClient() {
    String sql = " select count(1) from oauth_client_details where 1=1";
    return jdbcTemplate.queryForObject(sql, Integer.class);
  }

  public List<ClientDetails> listClient(final Integer start, final Integer limit) {
    String sql = " select * from oauth_client_details where 1=1  limit ?,?";
    return jdbcTemplate.query(sql, clientDetailsRowMapper, start, limit);
  }

  public Integer countToken() {
    String sql = " select count(1) from oauth_access_token where 1=1";
    return jdbcTemplate.queryForObject(sql, Integer.class);
  }

  public List<AccessToken> listToken(final Integer start, final Integer limit) {
    final String sql = " select * from oauth_access_token where 1=1 limit ?,?";
    return jdbcTemplate.query(sql, accessTokenRowMapper, start, limit);
  }

  public int delteClientDetail(String clientId) {
    final String sql = "delete from oauth_client_details where client_id = ? ";
    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, clientId);
      }
    });
  }

  public int deleteAccessToken(final String tokenId) {
    final String sql = " delete from oauth_access_token where token_id = ?";
    return jdbcTemplate.update(sql, new PreparedStatementSetter() {

      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, tokenId);
      }
    });
  }

}
