
Contributor License
-------------------

If this is your first time contributing to an Eclipse Foundation project, you'll need to sign the [Eclipse Foundation Contributor License Agreement][CLA].

- [Create an account](https://dev.eclipse.org/site_login/createaccount.php) on dev.eclipse.org
- Open your [Account Settings tab](https://dev.eclipse.org/site_login/myaccount.php#open_tab_accountsettings), enter your GitHub ID and click Update Account
- Read and [sign the CLA](https://projects.eclipse.org/user/sign/cla)
- Your git commits must be [signed off](https://wiki.eclipse.org/Development_Resources/Contributing_via_Git#Signing_off_on_a_commit)
- Use the exact same email address for your Eclipse account, your commit author, and your commit sign-off.

Making changes
--------------

Fork the repository in GitHub, make changes in your fork and submit a pull request.

Coding Style
------------

The gsc-ec-converter follows a coding style that is similar to [Google's Style Guide for Java][style-guide], but with curly braces on their own lines. 

Commit messages
---------------

- [Use the imperative mood][imperative-mood] as in "Fix bug" or "Add feature" rather than "Fixed bug" or "Added feature"
- [Mention the GitHub issue][github-issue] when relevant
- It's a good idea to follow the [advice in Pro Git](https://git-scm.com/book/ch5-2.html)
- Sign-off your commits using `git commit --signoff` or `git commit -s` for short

Pull requests
-------------

Excessive branching and merging can make git history confusing. With that in mind

- Squash your commits down to a few commits, or one commit, before submitting a pull request
- [Rebase your pull request changes on top of the current master][rebase]. Pull requests shouldn't include merge commits.

Submit your pull request when ready. Two checks will be kicked off automatically.

- IP Validation: Checks that all committers signed the Eclipse CLA and signed their commits.
- The standard GitHub check that the pull request has no conflicts with the base branch.

Make sure all the checks pass. One of the committers will take a look and provide feedback or merge your contribution.

That's it! Thanks for contributing to gsc-ec-converter!

[CLA]:             https://www.eclipse.org/legal/CLA.php
[style-guide]:     https://google.github.io/styleguide/javaguide.html
[rebase]:          https://github.com/edx/edx-platform/wiki/How-to-Rebase-a-Pull-Request
[imperative-mood]: https://github.com/git/git/blob/master/Documentation/SubmittingPatches
