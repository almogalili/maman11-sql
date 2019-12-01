
/* 1 */

SELECT eid 
FROM Employee as e
WHERE e.salary < 12000 AND
e.classification BETWEEN 1 AND 3;


/* 2 */

SELECT ename, salary
FROM Employee AS e, Onproject AS o, Project AS p
WHERE e.did = 1
AND e.eid = o.eid
AND p.pid = o.pid
AND p.pname = 'search'

/* 3 */

SELECT d.did, dname, SUM(distinct p.budget) AS totalBudget
FROM Department AS d, Project AS p
WHERE d.did = p.did
AND p.duedate > CURRENT_DATE
GROUP BY d.did

/* 4 */

SELECT d.did
from Department AS d, Budget AS b
WHERE d.did = b.did 
AND 2019 - b.byear <= 5
GROUP BY d.did
HAVING SUM(b.budget) < 2500000

/* 5 */

WITH workersOnProjects(avgSalary) AS
  (SELECT AVG(salary)
   FROM Employee AS e, Onproject
   WHERE e.eid IN
       (SELECT eid 
	   FROM Onproject)),
     notWorkersOnProjects(avgSalary) AS
  (SELECT AVG(salary)
   FROM Employee AS e, Onproject
   WHERE e.eid NOT IN
       (SELECT eid 
	   FROM Onproject) )
SELECT w.avgSalary - n.avgSalary
FROM notWorkersOnProjects AS n,
     workersOnProjects AS w

/* 6 */

with projectsWithWorkersLessThanThreeClass(pid, numOfProjects) AS
(
SELECT O.pid, count(O.pid)
	FROM Onproject AS O, Employee AS E
	WHERE E.eid = O.eid
	AND E.classification <=3 
	GROUP BY O.pid
)
SELECT P.budget, P.pname
FROM projectsWithWorkersLessThanThreeClass AS WL, Project AS P
WHERE WL.numOfProjects >= all(SELECT numOfProjects FROM projectsWithWorkersLessThanThreeClass)
AND WL.pid = P.pid

/* 7 */
/* 
   taking projects which e1.eid not working on them, and make sure that e2.eid also not working on them,
   and projects which e2.eid not working on them, and make sure that e1.eid also not working on them
    */

SELECT E1.eid, E2.eid
FROM Employee AS E1, Employee AS E2
/* this condition for ensure e1.eid != e2.eid */
WHERE E1.eid < E2.eid
/* this condition return projects which e2.eid and e1.eid not working on them */
AND E1.eid not in (

                    SELECT eid     /* This query return the projects that e2.eid not working on them*/
                    FROM Onproject 
                    WHERE pid not in (
                                       SELECT pid    /* This query return e2.eid projects */
                                       FROM Onproject
                                       WHERE e2.eid = eid 
                                                           )

                    )
/* this condition return projects which e2.eid and e1.eid not working on them */
AND E2.eid not in (

                    SELECT eid     /* This query return e1.eid which not working on proejcts */
                    FROM Onproject 
                    WHERE pid not in (
                                       SELECT pid    /* This query return e1.eid projects */
                                       FROM Onproject
                                       WHERE e1.eid = eid 
                                                           )

                   )					


















