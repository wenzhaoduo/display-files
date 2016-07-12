class xbox {
  include config

  file { "${config::basedir}/libexec/clean_script/clean_config.sh":
    content => template("clean_config.sh.erb"),
    mode => 644, owner => work, group => work,
  } ->
  file { "${config::basedir}/resin/conf/${config::prog_name}.xml":
    content => template("resin.xml.erb"),
    mode => 644, owner => work, group => work,
  }

  file { "${config::basedir}/resin/conf/resin.xml":
    ensure => "link",
    target => "${config::prog_name}.xml",
  }

  file { [ "/home/work/data/",
           "/home/work/data/${config::prog_name}/" ]:
    ensure => directory,
    mode => 755, owner => work, group => work,
  }
  file { [ "/home/work/log/",
           "/home/work/log/${config::prog_name}/" ]:
    ensure => directory,
    mode => 755, owner => work, group => work,
  } ->
  exec { 'chown -R work:work /home/work/log/${config::prog_name}/' :
    path => '/bin/:/usr/bin/'
  }
  package { ["python", "PyYAML", "python-argparse"]:
    ensure => installed;
  }

}

include xbox
