CREATE FUNCTION trigf1() RETURNS TRIGGER AS 
  $$
  
  DECLARE due_date DATE;
  
  BEGIN
  
  SELECT duedate INTO due_date
  FROM Project AS P 
  WHERE P.pid = NEW.pid;
  
  IF DATEDIFF(MONTH, NEW.fdate, due_date) >= 1
  THEN RETURN NEW;
  
  ELSE
  RAISE NOTICE 'The employee cannot begin start on this project, Please let him more than one month';
  RETURN NULL;
  
  END IF;
  
  END;
  
  $$
  LANGUAGE 'plpgsql';




CREATE TRIGGER T1
  BEFORE INSERT OR UPDATE 
  ON Onproject
  FOR EACH ROW
  EXECUTE PROCEDURE trigf1();
  
  
  
  
  