import { type IEmpleado } from '@/shared/model/empleado.model';
import { type IEspecialidad } from '@/shared/model/especialidad.model';

export interface IMedico {
  id?: number;
  matricula?: string;
  fechaMatriculacion?: Date | null;
  firmaDigital?: string | null;
  atiendeConsultorio?: boolean;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  empleado?: IEmpleado;
  especialidad?: IEspecialidad;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public matricula?: string,
    public fechaMatriculacion?: Date | null,
    public firmaDigital?: string | null,
    public atiendeConsultorio?: boolean,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public empleado?: IEmpleado,
    public especialidad?: IEspecialidad,
  ) {
    this.atiendeConsultorio = this.atiendeConsultorio ?? false;
    this.activo = this.activo ?? false;
  }
}
