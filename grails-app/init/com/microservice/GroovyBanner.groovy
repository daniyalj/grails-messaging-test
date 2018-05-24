package com.microservice

import org.springframework.boot.Banner
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.boot.ansi.AnsiStyle
import org.springframework.core.SpringVersion
import org.springframework.core.env.Environment

class GroovyBanner implements Banner {
    public final static def interestingEnvironmentProperties = [
            'PID',
            'grails.env',
            'grails.env.standalone',
            'info.app.grailsVersion',
            'info.app.name',
            'info.app.version',
            'java.class.version',
            'java.runtime.name',
            'java.runtime.version',
            'java.specification.name',
            'java.specification.vendor',
            'java.specification.version',
            'java.version',
            'java.vm.info',
            'java.vm.name',
            'java.vm.specification.name',
            'java.vm.specification.vendor',
            'java.vm.specification.version',
            'java.vm.vendor',
            'java.vm.version',
            'org.grails.MAIN_CLASS_NAME',
            'org.jboss.logging.provider',
            'os.arch',
            'os.name',
            'os.version',
            'sun.arch.data.model',
            'sun.cpu.endian',
            'sun.io.unicode.encoding',
            'sun.java.command',
            'sun.java.launcher',
            'sun.jnu.encoding',
            'sun.management.compiler'
    ]

    private static final String BANNER = '''
  
  Microservice
  
'''

    @Override
    void printBanner(Environment environment,
                     Class<?> sourceClass,
                     PrintStream out) {
        
        out.println ""
        out.println AnsiOutput.toString(AnsiColor.BLUE, AnsiStyle.BOLD, BANNER)

        out.println('Groovy / Java VM:')

        row '  Groovy version', GroovySystem.version, out
        row '  JVM version', System.getProperty('java.version'), out
        row '  Spring version', SpringVersion.getVersion(), out
        
        if (System.getProperty('info.app.grailsVersion')) {
            row '  Grails Version', System.getProperty('info.app.grailsVersion'), out
            row '  App Name', System.getProperty('info.app.name'), out
            row '  App Version', System.getProperty('info.app.version'), out
        }
        
        out.println()
        /**
        out.println('Java VM Properties:')
        
        System.properties.sort{ e1, e2 -> e1.key <=> e2.key }.each { prop ->
            def matchesPass = (prop.key =~ /(.*)(?i)(pass)(.*)/)
            def matchesSecret = (prop.key =~ /(.*)(?i)(secret)(.*)/)
            def matchesAuthToken = (prop.key =~ /(.*)(?i)(auth\_token)(.*)/)

            if (!(matchesPass.matches() || matchesSecret.matches() || matchesAuthToken.matches())) {
                row prop.key.toString(), prop.value, out
            } else {  // mask values
                row prop.key.toString(), 'XXX-masked-XXX', out
            }

        }
        **/

        out.println()
        out.println('Environment Variables:')
        
        /*
        System.getenv().sort{ e1, e2 -> e1.key <=> e2.key }.each { prop ->
            if (prop.key in RequiredEnvVars.serviceEnvironmentVariableList.collectEntries{ [(it.key):it.value] }) {
                def matchesPass = (prop.key =~ /(.*)(?i)(pass)(.*)/)
                def matchesSecret = (prop.key =~ /(.*)(?i)(secret)(.*)/)
                def matchesAuthToken = (prop.key =~ /(.*)(?i)(auth\_token)(.*)/)
                if (!(matchesPass.matches() || matchesSecret.matches() || matchesAuthToken.matches())) {
                    row prop.key.toString(), prop.value, out
                } else {  // mask values
                    row prop.key.toString(), 'XXX-masked-XXX', out
                }
            }
        }
        */
        
        def foundRequiredNotSet = false
        def requiredNotFoundList = []
        //RequiredEnvVars.serviceEnvironmentVariableList.sort{ e1, e2 -> e1.key <=> e2.key }.each { v ->
        RequiredEnvVars.serviceEnvironmentVariableList.each { v ->
            def keyName = v.key
            def value = System.getenv(keyName)

            //if (value != null || v.required) {

                if (v.required && value == null) {
                    foundRequiredNotSet = true
                    requiredNotFoundList << keyName
                }

                def matchesPass = (keyName =~ /(.*)(?i)(pass)(.*)/)
                def matchesSecret = (keyName =~ /(.*)(?i)(secret)(.*)/)
                if (matchesPass.matches() || matchesSecret.matches()) {
                    value = 'XXX-masked-XXX'
                }
                row '  ' + keyName, value != null ? value : '-- not set --', out
            //}
        }

        out.println()

        if (foundRequiredNotSet) {

            System.err.println()
            System.err.println("Required environment variable(s) not set:")
            requiredNotFoundList.each {
                System.err.println("  $it")
            }
            System.exit(-1)
        }
    }

    private void row(final String description, final value, final PrintStream out) {
        out.print AnsiOutput.toString(AnsiColor.GREEN, AnsiStyle.BOLD, description.padRight(40))
        out.print AnsiOutput.toString(AnsiColor.DEFAULT, ' :: ')
        out.println AnsiOutput.toString(AnsiColor.BLUE, AnsiStyle.NORMAL, value.toString().replace('\n', ''))

    }
}
