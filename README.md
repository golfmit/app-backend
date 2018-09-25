# Golfmit

One liner: Application that tells you what to practice next.

# Installation

1. Install serverless framework
    * npm install -g serverless
2. configure aws credetials
    * serverless config credentials --provider aws --key ** --secret **
    * now you can deploy code from local machine, credetials are stored under HOME/.aws/credetials
3. deploy on AWS
    * sls deploy
