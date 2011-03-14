Ok, it is really a bad thing to mimic the gov.va.med.vistalink.security
package in practice.  But, the reason is because the vistalink packages
are restricted to package-level access, making it impossible to extend
their vistalink authentication callbacks without making changes in
the vistalink project itself.  And, we really needed/wanted to
implement token based authentication for vistalink.

So, rather than create a new implementation, we decide it would be
better for now to just cheat a bit and create the same package
in our vistalink project and write the simple token basked
callback handler.
