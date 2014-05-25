SELECT * INTO OUTFILE '/tmp/body.txt'
        FIELDS TERMINATED BY ','
        FROM EXP_Table
        WHERE APP='Transaction';
