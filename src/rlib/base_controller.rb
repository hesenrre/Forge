##
# base_controller.rb is part of Forge Project.
#
# Copyright 2004,2007 LainSoft Foundation, Demetrio Cruz
#
# You may distribute under the terms of either the GNU General Public
# License or the Artistic License, as specified in the README file.
#
##
require "java"

include_class "org.lainsoft.forge.flow.nav.Command"

def camelize(str)
  str = str.split("_").collect{ |t| t.capitalize }.join
  return str
end

module Controller
  
  class Base < Command
    
    def method_missing(meth, arg=nil)
      if meth.id2name =~ /(real_path|getFileParameter|is_empty|real_path)/
        @helper.send(meth, arg)
      else
        raise NoMethodError, "no method #{meth} in Controller::Base"
      end
    end
    
    def params
      Params.new(@helper)
    end
    
    def session
      unless @session
        @session = @helper.getSession
        class << @session
          def [](attr)
            self.getAttribute(attr.to_s)
          end
          
          def []=(attr,value)
            self.setAttribute(attr.to_s,value)
          end
          
          def isNew?
            self.isNew
          end
        end
      end
      return @session
    end
    
    def request
      unless @request
        @request = @helper.getRequest
        class << @request
          def [](attr)
            self.getAttribute(attr.to_s)
          end          
          def []=(attr, value)
            self.setAttribute(attr.to_s,value)
          end
        end
      end
      return @request
    end
    
    def application
      unless @application
        @application = @helper.getApplicationContext
        class << @application
          def [](attr)
            self.getAttribute(attr.to_s)
          end
          
          def []=(attr,value)
            self.setAttribute(attr.to_s,value)
          end          
        end
      end
      return @application
    end
    
    def render(params)
      raise "Unknown render arguments" if params.size != 1
      response = nil
      params.each_pair do |k,v|
        unless  k.to_s =~ /partial|template|text|action/          
          raise "Unknown render argument #{k}" 
        end
        response = ":#{k} => #{v}"
      end
      return response
    end
    
    
    def execute(helper)
      @helper = helper      
      (stimulus = ((stimulus = request[:_stimulus]) =~/^\// ?  stimulus[1..-1] : stimulus).split(/\//)).shift if request[:_stimulus]
      action = (action = stimulus.shift) ? action : "index"
      return ":missing_action => <h1>unknown action</h1>\nNo action responded to #{action}" unless respond_to? action      
      response = ((response = send(action)).instance_of?(String) ? response : "")
      response = ((response.empty? and action == "index") ? ":template => index" : response)
      return response
    end
  end
  
  class Params
    
    def [](param_name)
      param_name = param_name.to_s if param_name
      return (param = search_fixed_params(param_name)) ? param : @params[param_name]
    end

    def search_fixed_params(param_name="")
      fixed_params = {}
      @params.each_key do |key|
        if (matcher = /#{param_name}\[([\w]+)\]/.match(key))
          fixed_params[matcher[1]]=@params[key]
        end
      end
      fixed_params.empty? ? nil : fixed_params
    end
    
    def requestParams(helper)
      names = helper.request.getParameterNames
      rparams = {}
      while(names.hasMoreElements)        
        rparams[name = names.next]=requestValueFor(name,helper)
      end
      return rparams
    end

    def requestValueFor(name, helper)
      value = helper.request.getParameterValues(name)      
      class << value
        def to_s
          inner = []
          self.each{|e| inner << e}
          "[#{inner.join(", ")}]"
        end
      end if value and value.length > 1      
      return ((value and value.length == 1) ? value[0] : value)
      
    end
    
    def initialize(helper)      
      @params = requestParams(helper)
    end
        
    def to_s
      r = []
      @params.each_pair{ |k,v| r << "#{k} => #{v}"}
      "{#{r.join(", ")}}"
    end

  end
end
