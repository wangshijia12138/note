package com.wangshijia.dao;

import com.wangshijia.pojo.User;
import com.wangshijia.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {

    @Test
    public void test1() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList) {
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }

    }

    @Test
    public void test2() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            List<User> userList = sqlSession.selectList("com.wangshijia.dao.UserMapper.getUserList");
            for (User user : userList) {
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void test3() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User userById = mapper.getUserById(1);
            System.out.println(userById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Test
    public void test4() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setId(4);
            user.setName("wwwww");
            user.setPwd("11223");
            int i = mapper.addUser(user);
            System.out.println(i > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.commit();
                sqlSession.close();
            }
        }
    }

    @Test
    public void test5() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setId(4);
            user.setName("xxxx");
            user.setPwd("xxxxx");
            int i = mapper.updateUser(user);
            System.out.println(i > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.commit();
                sqlSession.close();
            }
        }
    }

    @Test
    public void test6() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int i = mapper.deleteUserById(4);
            System.out.println(i > 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.commit();
                sqlSession.close();
            }
        }
    }
}
