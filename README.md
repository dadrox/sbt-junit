# SBT Junit Interface

This is an implementation of the testing-interface for SBT, that focuses on making junit test output useful.

Most output is suppressed, and more can enabled, if desired.

## Usage

### Dependency

Add `"com.dadrox" %% "sbt-junit" % "0.1" % "test"` to your dependencies.

### Add to your build 

Add `testFrameworks += new TestFramework("com.dadrox.sbt.junit.JunitFramework")` to your build.sbt

Or `testFrameworks += new TestFramework("com.dadrox.sbt.junit.JunitFramework")` to your full configuration.

## Configuration 

Default test options can be added to your full build configuration like this:

    testOptions in Test += Tests.Argument(framework, "-vo", "-tv")

System properties can be used to enable settings for specific contexts, such as Continuous Integration (recommended):

    > sbt -Dsbt.reports.traces.short= test

test-only and test-quick allow you to provide test options on the fly:

    > sbt "test-only [test suite pattern] -- -ts -vss"

    sbt> test-quick [test suite pattern] -- -vo

### Options
    
    <Long option>          <Short> <System Property>
    
    --no-color             -nc     -Dsbt.log.noformat=true
        Disable color output.
        It's usually better to completely disable colors in sbt output with -Dsbt.log.noformat=true
        
    --verbose-output       -vo     -Dsbt.reports.verbose.output=     
       Log test stdout/err inline at info level.
       
    --verbose-traces       -tv     -Dsbt.reports.traces.verbose=
      Log full test failure stacktraces at error level.
      
    --short-traces         -ts     -Dsbt.reports.traces.short=
        Log shortened test failure stacktraces at error level.
        
    --verbose-test-start   -vts    -Dsbt.reports.verbose.test.start=
        Log "Test Started" messages at info rather than debug.
        
    --verbose-test-pass    -vtp    -Dsbt.reports.verbose.test.pass=
        Log "Test Passed" messages at info rather than debug.
        
    --verbose-suite-start  -vss    -Dsbt.reports.verbose.suite.start=
        Log "Test Suite Started" messages at info rather than debug.

## Compatibility

Should work with the following scala versions:

 * 2.9.x
 * 2.10.x
 
Should work with the following SBT versions:

 * 0.11.x
 * 0.12.x

## Known Issues

 * There is no way to create list of all failed suites/test to log at the end of the test run. 

## License

Copyright (C) 2012-2013, Christopher Wood (dadrox)

Published under [BSD 2-Clause License](http://opensource.org/licenses/BSD-2-Clause)