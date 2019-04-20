package com.soprasteria.fitbit.service;

import com.soprasteria.fitbit.model.Departament;
import com.soprasteria.fitbit.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private JpaContext ctx;

    static final String QUERY_CURRENT_DEPARTMENT_RANKING = "select new com.soprasteria.fitbit.model.Departament(u.departament, sum(u.currentSteps)) " +
                    "from User u " +
                    "group by u.departament " +
                    "order by sum(u.currentSteps) desc";

    static final String QUERY_PREVIOUS_DEPARTMENT_RANKING = "select new com.soprasteria.fitbit.model.Departament(u.departament, sum(u.previusSteps)) " +
                    "from User u " +
                    "group by u.departament " +
                    "order by sum(u.previusSteps) desc";

    @Test
    public void getRankingByDepartamentTestA() {
        List<Departament> currrentDepartmentRanking = new ArrayList<>();
        Departament currentDepartamentA = new Departament("DEPARTMENT A", 3000);
        currrentDepartmentRanking.add(currentDepartamentA);
        Departament currentDepartamentB = new Departament("DEPARTMENT B", 1000);
        currrentDepartmentRanking.add(currentDepartamentB);

        List<Departament> previousDepartmentRanking = new ArrayList<>();
        Departament previousDepartamentA = new Departament("DEPARTMENT B", 50000);
        previousDepartmentRanking.add(previousDepartamentA);
        Departament previousDepartamentB = new Departament("DEPARTMENT A", 2000);
        previousDepartmentRanking.add(previousDepartamentB);

        TypedQuery<Departament> queryCurrentRanking = (TypedQuery<Departament>) Mockito.mock(TypedQuery.class);
        TypedQuery<Departament> queryPreviousRanking = (TypedQuery<Departament>) Mockito.mock(TypedQuery.class);


        Mockito.when(em.createQuery(QUERY_CURRENT_DEPARTMENT_RANKING, Departament.class)).thenReturn(queryCurrentRanking);
        Mockito.when(queryCurrentRanking.getResultList()).thenReturn(currrentDepartmentRanking);

        Mockito.when(em.createQuery(QUERY_PREVIOUS_DEPARTMENT_RANKING, Departament.class)).thenReturn(queryPreviousRanking);
        Mockito.when(queryPreviousRanking.getResultList()).thenReturn(previousDepartmentRanking);

        Mockito.when(ctx.getEntityManagerByManagedType(User.class)).thenReturn(em);

        UserService userService = new UserServiceImpl(ctx);

        List<Departament> departamentsRanking = userService.getRankingByDepartament();

        assertTrue(departamentsRanking.get(0).getTrend().equals(">") && departamentsRanking.get(1).getTrend().equals("<"));
    }

    @Test
    public void getRankingByDepartamentTestB() {
        List<Departament> currrentDepartmentRanking = new ArrayList<>();
        Departament currentDepartamentA = new Departament("DEPARTMENT A", 3000);
        currrentDepartmentRanking.add(currentDepartamentA);
        Departament currentDepartamentB = new Departament("DEPARTMENT B", 1000);
        currrentDepartmentRanking.add(currentDepartamentB);

        List<Departament> previousDepartmentRanking = new ArrayList<>();
        Departament previousDepartamentA = new Departament("DEPARTMENT A", 2000);
        previousDepartmentRanking.add(previousDepartamentA);
        Departament previousDepartamentB = new Departament("DEPARTMENT B", 1000);
        previousDepartmentRanking.add(previousDepartamentB);

        TypedQuery<Departament> queryCurrentRanking = (TypedQuery<Departament>) Mockito.mock(TypedQuery.class);
        TypedQuery<Departament> queryPreviousRanking = (TypedQuery<Departament>) Mockito.mock(TypedQuery.class);


        Mockito.when(em.createQuery(QUERY_CURRENT_DEPARTMENT_RANKING, Departament.class)).thenReturn(queryCurrentRanking);
        Mockito.when(queryCurrentRanking.getResultList()).thenReturn(currrentDepartmentRanking);

        Mockito.when(em.createQuery(QUERY_PREVIOUS_DEPARTMENT_RANKING, Departament.class)).thenReturn(queryPreviousRanking);
        Mockito.when(queryPreviousRanking.getResultList()).thenReturn(previousDepartmentRanking);

        Mockito.when(ctx.getEntityManagerByManagedType(User.class)).thenReturn(em);

        UserService userService = new UserServiceImpl(ctx);

        List<Departament> departamentsRanking = userService.getRankingByDepartament();

        assertTrue(departamentsRanking.get(0).getTrend().equals("=") && departamentsRanking.get(1).getTrend().equals("="));
    }


}
