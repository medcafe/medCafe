list = {
  "title" : "History for Observation",
  "id" : "urn:uuid:1fd4c54b-92d9-47d6-82fa-7f7ec82ebb",
  "totalResults" : "0",
  "link" : [
    {
      "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/history?_prior=2013-08-13T13:47:18Z&_offset=0&_count=20",
      "rel" : "self"
    },
    {
      "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/history?_prior=2013-08-13T13:47:18Z&_offset=0&_count=20",
      "rel" : "first"
    }
  ],
  "updated" : "2013-08-13T13:47:19Z",
  "entry" : [
    {
      "title" : "Observation \"f206\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f206",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f206/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:18Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "104177005"
                },
                "display" : {
                  "value" : "Blood culture for bacteria"
                }
              }
            ]
          },
          "valueCodeableConcept" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "8745002"
                },
                "display" : {
                  "value" : "Gram-positive bacteria"
                }
              }
            ]
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "POS"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-03-11T10:28:00+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "104177005"
                },
                "display" : {
                  "value" : "Blood culture for bacteria"
                }
              }
            ]
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f202"
            },
            "display" : {
              "value" : "Luigi Maas"
            }
          }
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f205\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f205",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f205/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:18Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "409402009"
                },
                "display" : {
                  "value" : "Epidermal Growth Factor Receptor"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "54"
            },
            "units" : {
              "value" : "mL/min"
            },
            "system" : {
              "value" : "http://snomed.info/id"
            },
            "code" : {
              "value" : "258859000"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "427038005"
                },
                "display" : {
                  "value" : "Negative for epidermal growth factor receptor expression (Non-small cell lung cancer)"
                }
              },
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "NEG"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-04-04T14:34:00+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "129266000"
                },
                "display" : {
                  "value" : "Measurement"
                }
              }
            ]
          },
          "identifier" : {
            "label" : {
              "value" : "eGFR value of Roel on April 2013 - 03720"
            },
            "system" : {
              "value" : "https://intranet.aumc.nl/labvalues"
            },
            "key" : {
              "value" : "1304-03720-eGFR"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f202"
            },
            "display" : {
              "value" : "Luigi Maas"
            }
          },
          "referenceRange" : [
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "426964009"
                    },
                    "display" : {
                      "value" : "Non-small cell carcinoma"
                    }
                  }
                ]
              },
              "rangeQuantity" : {
                "value" : {
                  "value" : "60"
                },
                "comparator" : {
                  "value" : ">"
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f204\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f204",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f204/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:18Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "365756002"
                },
                "display" : {
                  "value" : "Creatinine level"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "122"
            },
            "units" : {
              "value" : "umol/L"
            },
            "system" : {
              "value" : "http://snomed.info/id"
            },
            "code" : {
              "value" : "258814008"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "166717003"
                },
                "display" : {
                  "value" : "Serum creatinine raised"
                }
              },
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "H"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-04-04T14:34:00+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "113075003"
                },
                "display" : {
                  "value" : "Creatinine measurement, serum"
                }
              }
            ]
          },
          "identifier" : {
            "label" : {
              "value" : "Creatinine value of Roel on April 2013 - 03720"
            },
            "system" : {
              "value" : "https://intranet.aumc.nl/labvalues"
            },
            "key" : {
              "value" : "1304-03720-Creatinine"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f202"
            },
            "display" : {
              "value" : "Luigi Maas"
            }
          },
          "referenceRange" : [
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "260395002"
                    },
                    "display" : {
                      "value" : "Normal range"
                    }
                  }
                ]
              },
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "64"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "104"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f203\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f203",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f203/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "365722008"
                },
                "display" : {
                  "value" : "Bicarbonate level"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "28"
            },
            "units" : {
              "value" : "mmol/L"
            },
            "system" : {
              "value" : "http://snomed.info/id"
            },
            "code" : {
              "value" : "258813002"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "166698001"
                },
                "display" : {
                  "value" : "Serum bicarbonate level normal"
                }
              },
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "N"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-04-04T14:34:00+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "271239003"
                },
                "display" : {
                  "value" : "Serum bicarbonate measurement"
                }
              }
            ]
          },
          "identifier" : {
            "label" : {
              "value" : "Bicarbonaat value of Roel on April 2013 - 03720"
            },
            "system" : {
              "value" : "https://intranet.aumc.nl/labvalues"
            },
            "key" : {
              "value" : "1304-03720-Bicarbonate"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f202"
            },
            "display" : {
              "value" : "Luigi Maas"
            }
          },
          "referenceRange" : [
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "260395002"
                    },
                    "display" : {
                      "value" : "Normal range"
                    }
                  }
                ]
              },
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "22"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "29"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f202\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f202",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f202/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "415945006"
                },
                "display" : {
                  "value" : "Oral temperature"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "39"
            },
            "units" : {
              "value" : "degrees C"
            },
            "system" : {
              "value" : "http://snomed.info/id"
            },
            "code" : {
              "value" : "258710007"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "H"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-04-04T13:27:00+01:00"
          },
          "status" : {
            "value" : "withdrawn"
          },
          "reliability" : {
            "value" : "questionable"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "38266002"
                },
                "display" : {
                  "value" : "Body as a whole"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "89003005"
                },
                "display" : {
                  "value" : "Oral temperature taking"
                }
              }
            ]
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f201"
            }
          },
          "referenceRange" : [
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "386661006"
                    },
                    "display" : {
                      "value" : "Fever"
                    }
                  }
                ]
              },
              "rangeQuantity" : {
                "value" : {
                  "value" : "37.5"
                },
                "comparator" : {
                  "value" : ">"
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f201\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f201",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f201/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "60621009"
                },
                "display" : {
                  "value" : "Body mass index"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "31.31"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "75540009"
                },
                "display" : {
                  "value" : "High"
                }
              },
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "H"
                }
              }
            ]
          },
          "issued" : {
            "value" : "2013-04-04T13:27:00+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "38266002"
                },
                "display" : {
                  "value" : "Body as a whole"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "122869004"
                },
                "display" : {
                  "value" : "Measurement - action"
                }
              }
            ]
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f201"
            },
            "display" : {
              "value" : "Roel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f201"
            }
          },
          "referenceRange" : [
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "310252000"
                    },
                    "display" : {
                      "value" : "Low BMI"
                    }
                  }
                ]
              },
              "rangeQuantity" : {
                "value" : {
                  "value" : "20"
                },
                "comparator" : {
                  "value" : "<"
                }
              }
            },
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "412768003"
                    },
                    "display" : {
                      "value" : "Normal BMI"
                    }
                  }
                ]
              },
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "20"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "25"
                  }
                }
              }
            },
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "162863004"
                    },
                    "display" : {
                      "value" : "Overweight"
                    }
                  }
                ]
              },
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "25"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "30"
                  }
                }
              }
            },
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "162864005"
                    },
                    "display" : {
                      "value" : "Obesity"
                    }
                  }
                ]
              },
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "30"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "40"
                  }
                }
              }
            },
            {
              "meaning" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "162864005"
                    },
                    "display" : {
                      "value" : "Severe obesity"
                    }
                  }
                ]
              },
              "rangeQuantity" : {
                "value" : {
                  "value" : "40"
                },
                "comparator" : {
                  "value" : ">"
                }
              }
            }
          ],
          "component" : [
            {
              "name" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "410667008"
                    },
                    "display" : {
                      "value" : "Length"
                    }
                  }
                ]
              },
              "valueQuantity" : {
                "value" : {
                  "value" : "182"
                },
                "units" : {
                  "value" : "centimeter"
                },
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "258672001"
                }
              }
            },
            {
              "name" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://snomed.info/id"
                    },
                    "code" : {
                      "value" : "60621009"
                    },
                    "display" : {
                      "value" : "Weight"
                    }
                  }
                ]
              },
              "valueQuantity" : {
                "value" : {
                  "value" : "103.7"
                },
                "units" : {
                  "value" : "kilogram"
                },
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "258683005"
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f005\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f005",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f005/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "30350-3"
                },
                "display" : {
                  "value" : "Hemoglobin [Mass/?volume] in Venous blood"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "7.2"
            },
            "units" : {
              "value" : "g/dl"
            },
            "system" : {
              "value" : "http://unitsofmeasure.org"
            },
            "code" : {
              "value" : "g/dl"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "L"
                },
                "display" : {
                  "value" : "low"
                }
              }
            ]
          },
          "appliesPeriod" : {
            "start" : {
              "value" : "2013-04-02T10:30:10+01:00"
            },
            "end" : {
              "value" : "2013-04-05T10:30:10+01:00"
            }
          },
          "issued" : {
            "value" : "2013-04-03T15:30:10+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "308046002"
                },
                "display" : {
                  "value" : "Superficial forearm vein"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "120220003"
                },
                "display" : {
                  "value" : "Injection to forearm"
                }
              }
            ]
          },
          "identifier" : {
            "use" : {
              "value" : "official"
            },
            "system" : {
              "value" : "http://www.bmc.nl/zorgportal/identifiers/observations"
            },
            "key" : {
              "value" : "6327"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f001"
            },
            "display" : {
              "value" : "P. van de Heuvel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f005"
            },
            "display" : {
              "value" : "A. Langeveld"
            }
          },
          "referenceRange" : [
            {
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "7.5"
                  },
                  "units" : {
                    "value" : "g/dl"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "g/dl"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "10"
                  },
                  "units" : {
                    "value" : "g/dl"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "g/dl"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f004\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f004",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f004/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "28540-3"
                },
                "display" : {
                  "value" : "Erythrocyte concentration"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "18.7"
            },
            "units" : {
              "value" : "g/dl"
            },
            "system" : {
              "value" : "http://unitsofmeasure.org"
            },
            "code" : {
              "value" : "g/dl"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "A"
                },
                "display" : {
                  "value" : "abnormal"
                }
              }
            ]
          },
          "appliesPeriod" : {
            "start" : {
              "value" : "2013-04-02T10:30:10+01:00"
            },
            "end" : {
              "value" : "2013-04-05T10:30:10+01:00"
            }
          },
          "issued" : {
            "value" : "2013-04-03T15:30:10+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "308046002"
                },
                "display" : {
                  "value" : "Superficial forearm vein"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "120220003"
                },
                "display" : {
                  "value" : "Injection to forearm"
                }
              }
            ]
          },
          "identifier" : {
            "use" : {
              "value" : "official"
            },
            "system" : {
              "value" : "http://www.bmc.nl/zorgportal/identifiers/observations"
            },
            "key" : {
              "value" : "6326"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f001"
            },
            "display" : {
              "value" : "P. van de Heuvel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f005"
            },
            "display" : {
              "value" : "A. Langeveld"
            }
          },
          "referenceRange" : [
            {
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "32"
                  },
                  "units" : {
                    "value" : "g/dl"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "g/dl"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "36"
                  },
                  "units" : {
                    "value" : "g/dl"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "g/dl"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f003\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f003",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f003/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "11557-6"
                },
                "display" : {
                  "value" : "Carbon dioxide in blood"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "6.2"
            },
            "units" : {
              "value" : "mm[Hg]"
            },
            "system" : {
              "value" : "http://unitsofmeasure.org"
            },
            "code" : {
              "value" : "mm[Hg]"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "A"
                },
                "display" : {
                  "value" : "abnormal"
                }
              }
            ]
          },
          "appliesPeriod" : {
            "start" : {
              "value" : "2013-04-02T10:30:10+01:00"
            },
            "end" : {
              "value" : "2013-04-05T10:30:10+01:00"
            }
          },
          "issued" : {
            "value" : "2013-04-03T15:30:10+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "308046002"
                },
                "display" : {
                  "value" : "Superficial forearm vein"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "120220003"
                },
                "display" : {
                  "value" : "Injection to forearm"
                }
              }
            ]
          },
          "identifier" : {
            "use" : {
              "value" : "official"
            },
            "system" : {
              "value" : "http://www.bmc.nl/zorgportal/identifiers/observations"
            },
            "key" : {
              "value" : "6325"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f001"
            },
            "display" : {
              "value" : "P. van de Heuvel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f005"
            },
            "display" : {
              "value" : "A. Langeveld"
            }
          },
          "referenceRange" : [
            {
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "7.1"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "11.2"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f002\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f002",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f002/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "11555-0"
                },
                "display" : {
                  "value" : "Base excess in Blood by calculation"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "12.6"
            },
            "units" : {
              "value" : "mmol/l"
            },
            "system" : {
              "value" : "http://unitsofmeasure.org"
            },
            "code" : {
              "value" : "mmol/l"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "A"
                },
                "display" : {
                  "value" : "abnormal"
                }
              }
            ]
          },
          "appliesPeriod" : {
            "start" : {
              "value" : "2013-04-02T10:30:10+01:00"
            },
            "end" : {
              "value" : "2013-04-05T10:30:10+01:00"
            }
          },
          "issued" : {
            "value" : "2013-04-03T15:30:10+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "308046002"
                },
                "display" : {
                  "value" : "Superficial forearm vein"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "120220003"
                },
                "display" : {
                  "value" : "Injection to forearm"
                }
              }
            ]
          },
          "identifier" : {
            "use" : {
              "value" : "official"
            },
            "system" : {
              "value" : "http://www.bmc.nl/zorgportal/identifiers/observations"
            },
            "key" : {
              "value" : "6324"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f001"
            },
            "display" : {
              "value" : "P. van de Heuvel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f005"
            },
            "display" : {
              "value" : "A. Langeveld"
            }
          },
          "referenceRange" : [
            {
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "7.1"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "11.2"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"f001\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f001",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f001/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "2339-0"
                },
                "display" : {
                  "value" : "glucose observation from blood system"
                }
              }
            ]
          },
          "valueQuantity" : {
            "value" : {
              "value" : "6.3"
            },
            "units" : {
              "value" : "mmol/l"
            },
            "system" : {
              "value" : "http://unitsofmeasure.org"
            },
            "code" : {
              "value" : "mmol/l"
            }
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "A"
                },
                "display" : {
                  "value" : "abnormal"
                }
              }
            ]
          },
          "appliesPeriod" : {
            "start" : {
              "value" : "2013-04-02T09:30:10+01:00"
            },
            "end" : {
              "value" : "2013-04-05T09:30:10+01:00"
            }
          },
          "issued" : {
            "value" : "2013-04-03T15:30:10+01:00"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "bodySite" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "308046002"
                },
                "display" : {
                  "value" : "Superficial forearm vein"
                }
              }
            ]
          },
          "method" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://snomed.info/id"
                },
                "code" : {
                  "value" : "120220003"
                },
                "display" : {
                  "value" : "Injection to forearm"
                }
              }
            ]
          },
          "identifier" : {
            "use" : {
              "value" : "official"
            },
            "system" : {
              "value" : "http://www.bmc.nl/zorgportal/identifiers/observations"
            },
            "key" : {
              "value" : "6323"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@f001"
            },
            "display" : {
              "value" : "P. van de Heuvel"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@f005"
            },
            "display" : {
              "value" : "A. Langeveld"
            }
          },
          "referenceRange" : [
            {
              "rangeRange" : {
                "low" : {
                  "value" : {
                    "value" : "3.1"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                },
                "high" : {
                  "value" : {
                    "value" : "6.2"
                  },
                  "units" : {
                    "value" : "mmol/l"
                  },
                  "system" : {
                    "value" : "http://unitsofmeasure.org"
                  },
                  "code" : {
                    "value" : "mmol/l"
                  }
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">--No Summary for this resource--</div>"
    },
    {
      "title" : "Observation \"example\" Version \"1\"",
      "id" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@example",
      "link" : [
        {
          "href" : "http://hl7connect.healthintersections.com.au/svc/fhir/observation/@example/history/@1",
          "rel" : "self"
        }
      ],
      "updated" : "2013-08-09T10:47:11Z",
      "published" : "2013-08-13T13:47:19Z",
      "author" : [
        {
          "name" : "110.143.187.242"
        }
      ],
      "content" : {
        "Observation" : {
          "text" : {
            "status" : {
              "value" : "generated"
            },
            "div" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">Sept 17, 2012: Blood pressure 107/65 (normal)</div>"
          },
          "name" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://loinc.org"
                },
                "code" : {
                  "value" : "55284-4"
                },
                "display" : {
                  "value" : "Blood pressure systolic and diastolic"
                }
              }
            ]
          },
          "interpretation" : {
            "coding" : [
              {
                "system" : {
                  "value" : "http://hl7.org/fhir/v2/0078"
                },
                "code" : {
                  "value" : "N"
                },
                "display" : {
                  "value" : "Normal (applies to non-numeric results)"
                }
              }
            ]
          },
          "appliesDateTime" : {
            "value" : "2012-09-17"
          },
          "status" : {
            "value" : "final"
          },
          "reliability" : {
            "value" : "ok"
          },
          "identifier" : {
            "system" : {
              "value" : "urn:ietf:rfc:3986"
            },
            "key" : {
              "value" : "187e0c12-8dd2-67e2-99b2-bf273c878281"
            }
          },
          "subject" : {
            "type" : {
              "value" : "Patient"
            },
            "reference" : {
              "value" : "patient/@example"
            }
          },
          "performer" : {
            "type" : {
              "value" : "Practitioner"
            },
            "reference" : {
              "value" : "practitioner/@example"
            }
          },
          "component" : [
            {
              "name" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://loinc.org"
                    },
                    "code" : {
                      "value" : "8480-6"
                    },
                    "display" : {
                      "value" : "Systolic blood pressure"
                    }
                  }
                ]
              },
              "valueQuantity" : {
                "value" : {
                  "value" : "107"
                },
                "units" : {
                  "value" : "mm[Hg]"
                }
              }
            },
            {
              "name" : {
                "coding" : [
                  {
                    "system" : {
                      "value" : "http://loinc.org"
                    },
                    "code" : {
                      "value" : "8462-4"
                    },
                    "display" : {
                      "value" : "Diastolic blood pressure"
                    }
                  }
                ]
              },
              "valueQuantity" : {
                "value" : {
                  "value" : "65"
                },
                "units" : {
                  "value" : "mm[Hg]"
                }
              }
            }
          ]
        }
      },
      "summary" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<div xmlns=\"http://www.w3.org/1999/xhtml\">Sept 17, 2012: Blood pressure 107/65 (normal)</div>"
   }
  ]
}