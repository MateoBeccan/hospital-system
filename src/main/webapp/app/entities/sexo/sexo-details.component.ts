import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ISexo } from '@/shared/model/sexo.model';

import SexoService from './sexo.service';

export default defineComponent({
  name: 'SexoDetails',
  setup() {
    const sexoService = inject('sexoService', () => new SexoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const sexo: Ref<ISexo> = ref({});

    const retrieveSexo = async sexoId => {
      try {
        const res = await sexoService().find(sexoId);
        sexo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sexoId) {
      retrieveSexo(route.params.sexoId);
    }

    return {
      alertService,
      sexo,

      previousState,
    };
  },
});
