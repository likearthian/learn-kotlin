package com.indosatppi.kcli.process

import com.indosatppi.kcli.logger.logger
import com.github.ajalt.clikt.output.TermUi.echo
import com.indosatppi.kcli.logger.logger
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

class Hadoop(val sess: String) {
    fun UploadHdfs() {
        val fs = createHdfs()
        val files = fs.listFiles(Path("/"), true);
        while (files.hasNext()) {
            val file = files.next()
            logger.info{file.getPath()}

        }
    }

    fun createHdfs(): FileSystem {
        val conf = createHadoopConfig()
        return FileSystem.get(conf)
    }

    private fun createHadoopConfig(): Configuration {
        System.setProperty("java.security.krb5.realm", "OFFICE.CORP.INDOSAT.COM");
        System.setProperty("java.security.krb5.kdc", "poaddc02w.office.corp.indosat.com");

        val conf = Configuration()
        conf.set("fs.defaultFS", "hdfs://hdp2-amg0001:8020")
        conf.set("hadoop.security.authentication", "kerberos")
        conf.set("hadoop.security.authorization", "true")

        // server principal
        // the kerberos principle that the namenode is using
        conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/*@OFFICE.CORP.INDOSAT.COM");

        UserGroupInformation.setConfiguration(conf)
        UserGroupInformation.loginUserFromKeytab(
            "hdp_ppi1@OFFICE.CORP.INDOSAT.COM",
            "/Users/ziska/Projects/go/indosatppi/poc/hdfs/keys/hdp_ppi1.keytab"
        )
    }
}