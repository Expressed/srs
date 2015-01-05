set serveroutput on
/*
Utilities file defines addition support files for the main package 
including sequences and triggers 
*/



/*
Sequence is used to keep track of log numbers 
*/

begin
execute immediate 'drop sequence log_seq';

execute immediate
'create sequence log_seq
MAXVALUE 9999
start with 1000
increment by 1
nocache
nocycle';

end;
/


/*
custom trigger monitors the Students table
*/

create or replace trigger log_student_trig
after insert or delete on students
for each row
begin
        IF INSERTING THEN
                insert into logs values(log_seq.nextval, user, sysdate, 
'students', 'Insert', :NEW.sid);
        ELSIF DELETING THEN
                delete from enrollments where sid = :OLD.sid;
                insert into logs values(log_seq.nextval, user, sysdate, 
'students', 'Delete', :OLD.sid);
        END IF;
end;
/

/*
Custom trigger monitors the enrollments table
*/

create or replace trigger log_student_enroll_trig
after insert or delete on enrollments
for each row
declare
classLimit classes.LIMIT%type;
classSize classes.class_size%type;
SQL_EXP_CLASS_LIMIT_REACHED exception;
PRAGMA EXCEPTION_INIT(SQL_EXP_CLASS_LIMIT_REACHED, -20006);
SQL_EXP_CLASS_SIZE_NEG exception;
PRAGMA EXCEPTION_INIT(SQL_EXP_CLASS_SIZE_NEG, -20007);
begin
        IF INSERTING THEN
                select limit, class_size into classLimit, classSize from 
classes where classid = :NEW.classid;
                if classSize >= classLimit then
                  raise_application_error(-20006, 'The class is closed.');
                end if;
                update classes set class_size = class_size + 1 where 
classid = :NEW.classid;
                insert into logs values(log_seq.nextval, user, sysdate, 
'enrollments', 'Insert', :NEW.sid||:NEW.classid);
        ELSIF DELETING THEN
                select limit, class_size into classLimit, classSize from 
classes where classid = :OLD.classid;
                if classSize = 0  then
                  raise_application_error(-20007, 'The class size cannot be negative.');
                end if;
                update classes set class_size = class_size - 1 where 
classid = :OLD.classid;
                insert into logs values(log_seq.nextval, user, sysdate, 
'enrollments', 'Delete', :OLD.sid||:OLD.classid);
        END IF;
end;
/
show error
